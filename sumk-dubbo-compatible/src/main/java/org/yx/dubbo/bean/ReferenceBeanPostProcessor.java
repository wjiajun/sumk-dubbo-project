package org.yx.dubbo.bean;

import com.google.common.collect.Maps;
import org.yx.annotation.spec.parse.SpecParsers;
import org.yx.bean.IOC;
import org.yx.bean.InnerIOC;
import org.yx.bean.Loader;
import org.yx.dubbo.main.DubboStartConstants;
import org.yx.dubbo.spec.DubboBeanSpec;
import org.yx.dubbo.spec.DubboBuiltIn;
import org.yx.dubbo.utils.ResolveUtils;
import org.yx.main.StartContext;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author : wjiajun
 * @Reference
 * @description:
 */
public class ReferenceBeanPostProcessor {

    private static ConcurrentMap<String, ReferenceBean<?>> referenceBeanCache = Maps.newConcurrentMap();

    public static synchronized void init() {
        if (StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO) == null
                || Objects.equals(StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO), false)) {
            return;
        }

        // 获取Dubbo Service Bean
//        List<Object> serviceBeanList = ServiceClassPostProcessor.getServiceBeanNameSet()
//                .stream()
//                .map(IOC::get)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());


//        InnerIOC.beans().forEach(bean -> {
//            DubboBeanSpec parse = SpecParsers.parse(bean.getClass(), DubboBuiltIn.DUBBO_PARSER);
//            if (parse == null) {
//                return;
//            }
//            try {
//                injectProperties(bean);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
        InnerIOC.beans().forEach(bean -> {
            try {
                injectProperties(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取代理后的DubboRefernce
     */
    public static Object doGetInjectedBean(Object bean, Field f) throws Exception {
        DubboBeanSpec referenceSpec = SpecParsers.parse(bean, f, DubboBuiltIn.DUBBO_REFERENCE_PARSER);
        if(referenceSpec == null) {
            return null;
        }

        String serviceBeanName = ResolveUtils.generateServiceBeanName(referenceSpec);

        ReferenceBean<?> referenceBean = buildReferenceBeanIfAbsent(referenceSpec, f.getType());

        boolean localServiceBean = isLocalServiceBean(serviceBeanName, referenceBean, referenceSpec);
        prepareReferenceBean(serviceBeanName, referenceBean, localServiceBean);
        registerReferenceBean(referenceBean, referenceSpec);

        // 创建代理对象
        return referenceBean.get();
    }

    private static void registerReferenceBean(ReferenceBean referenceBean, DubboBeanSpec referenceSpec) {

        String referenceBeanName = ResolveUtils.generateReferenceBeanName(referenceSpec);

        if (IOC.get(referenceBeanName) == null) {
            InnerIOC.putBean(referenceBeanName, referenceBean);
        }
    }

    private static boolean isLocalServiceBean(String referencedBeanName, ReferenceBean<?> referenceBean, DubboBeanSpec referencedSpec) {
        return IOC.get(referencedBeanName) != null && !isRemoteReferenceBean(referenceBean, referencedSpec);
    }

    private static boolean isRemoteReferenceBean(ReferenceBean referenceBean, DubboBeanSpec referencedSpec) {
        boolean remote = Boolean.FALSE.equals(referenceBean.isInjvm()) || Boolean.FALSE.equals(referencedSpec.getAnnotationAttributes().getBoolean("injvm"));
        return remote;
    }

    private static ReferenceBean<?> buildReferenceBeanIfAbsent(DubboBeanSpec referenceSpec, Class<?> referencedType)
            throws Exception {

        String referenceBeanName = ResolveUtils.generateReferenceBeanName(referenceSpec);

        ReferenceBean<?> referenceBean = referenceBeanCache.get(referenceBeanName);

        // 然后，如果不存在，则进行创建。然后，添加到 referenceBeanCache 缓存中。
        if (referenceBean == null) {
            ReferenceBeanBuilder beanBuilder = ReferenceBeanBuilder
                    .create(referenceSpec)
                    .interfaceClass(referencedType);
            referenceBean = beanBuilder.build();
            referenceBeanCache.put(referenceBeanName, referenceBean);
        } else if (!referencedType.isAssignableFrom(referenceBean.getInterfaceClass())) {
            throw new IllegalArgumentException("reference bean name " + referenceBeanName + " has been duplicated, but interfaceClass " +
                    referenceBean.getInterfaceClass().getName() + " cannot be assigned to " + referencedType.getName());
        }
        return referenceBean;
    }

    public static void injectProperties(Object bean) throws Exception {
        Class<?> tempClz = bean.getClass();
        while (tempClz != null && (!tempClz.getName().startsWith(Loader.JAVA_PRE))) {

            Field[] fs = tempClz.getDeclaredFields();
            for (Field f : fs) {
                Object target;
                target = doGetInjectedBean(bean, f);
                if (target == null) {
                    continue;
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

    private static void prepareReferenceBean(String referencedBeanName, ReferenceBean referenceBean, boolean localServiceBean) {
        if (localServiceBean) { // If the local @Service Bean exists
            referenceBean.setInjvm(Boolean.TRUE);
            exportServiceBeanIfNecessary(referencedBeanName); // If the referenced ServiceBean exits, export it immediately
        }
    }


    private static void exportServiceBeanIfNecessary(String referencedBeanName) {
        if (IOC.get(referencedBeanName) != null) {
            ServiceBean<?> serviceBean = IOC.get(referencedBeanName, ServiceBean.class);
            if (!serviceBean.isExported()) {
                serviceBean.export();
            }
        }
    }
}
