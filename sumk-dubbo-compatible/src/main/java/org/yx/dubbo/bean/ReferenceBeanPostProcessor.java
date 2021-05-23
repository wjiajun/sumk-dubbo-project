package org.yx.dubbo.bean;

import com.google.common.collect.Maps;
import org.yx.annotation.spec.parse.SpecParsers;
import org.yx.bean.IOC;
import org.yx.bean.InnerIOC;
import org.yx.bean.Loader;
import org.yx.dubbo.config.DubboConst;
import org.yx.dubbo.spec.DubboBeanSpec;
import org.yx.dubbo.spec.DubboBuiltIn;
import org.yx.dubbo.utils.ResolveUtils;
import org.yx.main.StartContext;
import org.yx.util.S;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : wjiajun
 */
public class ReferenceBeanPostProcessor {

    private final static ConcurrentMap<String, ReferenceBean<?>> REFERENCE_BEAN_CACHE = Maps.newConcurrentMap();

    public static synchronized void init() {
        if (StartContext.inst().get(DubboConst.ENABLE_DUBBO) == null
                || Objects.equals(StartContext.inst().get(DubboConst.ENABLE_DUBBO), false)) {
            return;
        }

        // 由于plugin只能最后执行，所以服务提前注入，统一最后注入
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

        ReferenceBean<?> referenceBean = buildReferenceBeanIfAbsent(referenceSpec, f);

        boolean localServiceBean = isLocalServiceBean(serviceBeanName, referenceBean, referenceSpec);
        prepareReferenceBean(serviceBeanName, referenceBean, localServiceBean);
        registerReferenceBean(referenceBean, referenceSpec);

        // 创建代理对象
        return referenceBean.get();
    }

    private static void registerReferenceBean(ReferenceBean<?> referenceBean, DubboBeanSpec referenceSpec) throws Exception {

        String referenceBeanName = ResolveUtils.generateReferenceBeanName(referenceSpec);

        // 防止=被被截取
        String referenceBeanNameKey = S.hash().digestByteToString(referenceBeanName.getBytes(StandardCharsets.UTF_8));
        if (IOC.get(referenceBeanNameKey) == null) {
            InnerIOC.putBean(referenceBeanNameKey, referenceBean);
        }
    }

    private static boolean isLocalServiceBean(String referencedBeanName, ReferenceBean<?> referenceBean, DubboBeanSpec referencedSpec) {
        return IOC.get(referencedBeanName) != null && !isRemoteReferenceBean(referenceBean, referencedSpec);
    }

    private static boolean isRemoteReferenceBean(ReferenceBean<?> referenceBean, DubboBeanSpec referencedSpec) {
        return Boolean.FALSE.equals(referenceBean.isInjvm()) || Boolean.FALSE.equals(referencedSpec.getAnnotationAttributes().getBoolean("injvm"));
    }

    private static ReferenceBean<?> buildReferenceBeanIfAbsent(DubboBeanSpec referenceSpec, Field referencedField)
            throws Exception {

        Class<?> referencedType = referencedField.getType();

        String referenceBeanName = ResolveUtils.generateReferenceBeanName(referenceSpec);

        String referenceBeanNameKey = S.hash().digestByteToString(referenceBeanName.getBytes(StandardCharsets.UTF_8));
        ReferenceBean<?> referenceBean = REFERENCE_BEAN_CACHE.get(referenceBeanNameKey);

        // 然后，如果不存在，则进行创建。然后，添加到 referenceBeanCache 缓存中。
        if (referenceBean == null) {
            ReferenceBeanBuilder beanBuilder = ReferenceBeanBuilder
                    .create(referenceSpec)
                    .interfaceClass(referencedType);
            referenceBean = beanBuilder.build();
            REFERENCE_BEAN_CACHE.put(referenceBeanNameKey, referenceBean);
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

    private static void prepareReferenceBean(String referencedBeanName, ReferenceBean<?> referenceBean, boolean localServiceBean) {
        // If the local @Service Bean exists
        if (localServiceBean) {
            referenceBean.setInjvm(Boolean.TRUE);
            // If the referenced ServiceBean exits, export it immediately
            exportServiceBeanIfNecessary(referencedBeanName);
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
