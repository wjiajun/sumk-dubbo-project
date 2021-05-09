package org.yx.dubbo.bean;

import org.slf4j.Logger;
import org.springframework.util.StringUtils;
import org.yx.annotation.spec.InjectSpec;
import org.yx.annotation.spec.Specs;
import org.yx.annotation.spec.parse.SpecParsers;
import org.yx.bean.BeanKit;
import org.yx.bean.IOC;
import org.yx.bean.InnerIOC;
import org.yx.bean.Loader;
import org.yx.common.scaner.ClassScaner;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.dubbo.main.DubboStartConstants;
import org.yx.dubbo.spec.DubboBeanSpec;
import org.yx.dubbo.spec.DubboBuiltIn;
import org.yx.exception.SimpleSumkException;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.main.StartContext;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : wjiajun
 * @description:
 */
public class ServiceClassPostProcessor {

    private static final Logger logger = Logs.ioc();

    public static synchronized void init() {
        if (StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO) == null
                || Objects.equals(StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO), false)) {
            return;
        }

        List<String> dubboPackageNames = Arrays.stream(DubboStartConstants.DUBBO_PACKAGES)
                .map(AppInfo::getLatin)
                .filter(StringUtil::isNotEmpty)
                .map(i -> StringUtil.splitAndTrim(i, Const.COMMA, Const.SEMICOLON))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Collection<String> dubboClasses = ClassScaner.listClasses(dubboPackageNames);

        List<Class<?>> clazzList = new ArrayList<>(dubboClasses.size());
        for (String c : dubboClasses) {

            try {
                if (logger.isTraceEnabled()) {
                    logger.trace("{} begin loading");
                }

                Class<?> clz = Loader.loadClassExactly(c);
                if ((clz.getModifiers() & (Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.PUBLIC
                        | Modifier.INTERFACE)) != Modifier.PUBLIC || clz.isAnonymousClass() || clz.isLocalClass()
                        || clz.isAnnotation() || clz.isEnum()) {
                    continue;
                }
                clazzList.add(clz);
            } catch (LinkageError e) {
                logger.error("{}加载失败，原因是:{}", c, e.getLocalizedMessage());
                throw e;
            } catch (Exception e) {
                logger.error(c + "加载失败", e);
            }
        }
        registerServiceBeans(clazzList);
    }

    private static void registerServiceBeans(List<Class<?>> clazzList) {
        int beanSize = clazzList.size();
        Map<String, Object> beanMap = new HashMap<>(beanSize + beanSize / 3);

        clazzList.forEach(clazz -> {
            DubboBeanSpec parse = SpecParsers.parse(clazz, DubboBuiltIn.DUBBO_PARSER);
            if (parse == null) {
                return;
            }
            try {
                // 注入sumk
                beanMap.putIfAbsent(BeanKit.resloveBeanName(clazz), InnerIOC.putClass(BeanKit.resloveBeanName(clazz), clazz));
            } catch (Exception e) {
                throw new SumkException(-345365, "IOC error on " + clazz, e);
            }
            // 重新生成bean名称
            String beanName = generateServiceBeanName(parse);
            try {
                beanMap.putIfAbsent(beanName, InnerIOC.putClass(beanName, clazz));
            } catch (Exception e) {
                throw new SumkException(-345365, "IOC error on " + clazz, e);
            }
        });

        beanMap.values().forEach(bean -> {
            try {
                injectProperties(bean);
            } catch (Exception e) {
                throw new SumkException(-345365, "IOC error on " + bean, e);
            }
        });

        beanMap.entrySet().forEach(e -> e.setValue(null));
    }

    private static String generateServiceBeanName(DubboBeanSpec dubboBeanSpec) {
        StringBuilder beanNameBuilder = new StringBuilder("ServiceBean");
        // Required
        append(beanNameBuilder, dubboBeanSpec.getInterfaceName());
        // Optional
        append(beanNameBuilder, dubboBeanSpec.getVersion());
        append(beanNameBuilder, dubboBeanSpec.getGroup());
        // Build and remove last ":"
        String rawBeanName = beanNameBuilder.toString();
        return rawBeanName;
    }

    private static void append(StringBuilder builder, String value) {
        if (StringUtils.hasText(value)) {
            if (value.contains("${")) {
                String replace = value.replace("${", "").replace("}", "");
                builder.append(":").append(AppInfo.getLatin(replace, ""));
                return;
            }
            builder.append(":").append(value);
        }
    }

    private static void injectProperties(Object bean) throws Exception {
        Class<?> tempClz = bean.getClass();
        Class<?> fieldType;
        while (tempClz != null && (!tempClz.getName().startsWith(Loader.JAVA_PRE))) {

            Field[] fs = tempClz.getDeclaredFields();
            for (Field f : fs) {
                InjectSpec inject = Specs.extractInject(bean, f);
                if (inject == null) {
                    continue;
                }
                fieldType = f.getType();
                Object target;
                if (fieldType.isArray()) {
                    target = getArrayField(f, bean, inject.allowEmpty());
                } else if (List.class == fieldType || Collection.class == fieldType) {
                    target = getListField(f, bean, inject.allowEmpty());
                } else {
                    target = getBean(f);
                }
                if (target == null) {
                    if (inject.allowEmpty()) {
                        continue;
                    }
                    throw new SimpleSumkException(-235435658,
                            bean.getClass().getName() + "." + f.getName() + " cannot injected.");
                }
                injectField(f, bean, target);
            }
            tempClz = tempClz.getSuperclass();
        }
    }

    private static void injectField(Field f, Object bean, Object target) throws IllegalAccessException {
        boolean access = f.isAccessible();
        if (!access) {
            f.setAccessible(true);
        }
        f.set(bean, target);
    }

    private static Object[] getArrayField(Field f, Object bean, boolean allowEmpty) {
        Class<?> clz = f.getType().getComponentType();
        List<?> target = IOC.getBeans(clz);
        if (target == null || target.isEmpty()) {
            if (!allowEmpty) {
                throw new SimpleSumkException(-235435651, bean.getClass().getName() + "." + f.getName() + " is empty.");
            }
            return (Object[]) Array.newInstance(clz, 0);
        }
        return target.toArray((Object[]) Array.newInstance(clz, target.size()));
    }

    private static List<?> getListField(Field f, Object bean, boolean allowEmpty) throws ClassNotFoundException {
        String genericName = f.getGenericType().getTypeName();
        if (genericName == null || genericName.isEmpty() || !genericName.contains("<")) {
            throw new SimpleSumkException(-239845611,
                    bean.getClass().getName() + "." + f.getName() + "is List,but not List<T>");
        }
        genericName = genericName.substring(genericName.indexOf("<") + 1, genericName.length() - 1);
        Class<?> clz = Loader.loadClassExactly(genericName);
        if (clz == Object.class) {
            throw new SimpleSumkException(-23984568,
                    bean.getClass().getName() + "." + f.getName() + ": beanClz of @Inject in list type cannot be null");
        }
        List<?> target = IOC.getBeans(clz);
        if (target == null || target.isEmpty()) {
            if (!allowEmpty) {
                throw new SimpleSumkException(-235435652, bean.getClass().getName() + "." + f.getName() + " is empty.");
            }
            return Collections.emptyList();
        }
        return CollectionUtil.unmodifyList(target.toArray());
    }

    private static Object getBean(Field f) {
        String name = f.getName();
        Class<?> clz = f.getType();

        List<?> list = IOC.getBeans(name, clz);
        if (list.size() == 1) {
            return list.get(0);
        }
        if (list.size() > 1) {
            for (Object obj : list) {

                if (clz == BeanKit.getTargetClass(obj)) {
                    return obj;
                }
            }
        }
        return IOC.get(clz);
    }
}
