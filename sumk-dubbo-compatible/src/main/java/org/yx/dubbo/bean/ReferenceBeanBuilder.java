package org.yx.dubbo.bean;

import com.google.common.collect.Iterables;
import org.apache.dubbo.common.utils.Assert;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.annotation.Method;
import org.yx.bean.IOC;
import org.yx.dubbo.annotation.AnnotationAttributes;
import org.yx.dubbo.annotation.AnnotationResolver;
import org.yx.dubbo.spec.DubboBeanSpec;
import org.yx.exception.SumkException;
import org.yx.util.CollectionUtil;
import org.yx.util.kit.Asserts;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

class ReferenceBeanBuilder extends AnnotatedInterfaceConfigBeanBuilder<ReferenceBean> {

    // Ignore those fields
    static final String[] IGNORE_FIELD_NAMES = new String[]{"application", "module", "consumer", "monitor", "registry"};

    private ReferenceBeanBuilder(DubboBeanSpec dubboBeanSpec) {
        super(dubboBeanSpec);
    }

    private void configureInterface(DubboBeanSpec dubboBeanSpec, ReferenceBean referenceBean) {
        Boolean generic = dubboBeanSpec.getAnnotationAttributes().getBoolean("generic");
        if (generic) {
            // it's a generic reference
            String interfaceClassName = dubboBeanSpec.getAnnotationAttributes().getString("interfaceName");
            Assert.notEmptyString(interfaceClassName, "@Reference interfaceName() must be present when reference a generic service!");
            referenceBean.setInterface(interfaceClassName);
            return;
        }

        Class<?> serviceInterfaceClass = AnnotationResolver.resolveServiceInterfaceClass(dubboBeanSpec.getAnnotationAttributes(), interfaceClass);

        Asserts.requireTrue(serviceInterfaceClass.isInterface(), "The class of field or method that was annotated @Reference is not an interface!");

        referenceBean.setInterface(serviceInterfaceClass);

    }


    private void configureConsumerConfig(DubboBeanSpec dubboBeanSpec, ReferenceBean<?> referenceBean) {

        String consumerBeanName = dubboBeanSpec.getAnnotationAttributes().getString("consumer");

        ConsumerConfig consumerConfig = Iterables.getFirst(IOC.getBeans(consumerBeanName, ConsumerConfig.class), null);
        referenceBean.setConsumer(consumerConfig);
    }

    void configureMethodConfig(DubboBeanSpec dubboBeanSpec, ReferenceBean<?> referenceBean) {
        Method[] methods = dubboBeanSpec.getAnnotationAttributes().getAnnotationArray("methods", Method.class);
        List<MethodConfig> methodConfigs = MethodConfig.constructMethodConfig(methods);
        if (!methodConfigs.isEmpty()) {
            referenceBean.setMethods(methodConfigs);
        }
    }

    @Override
    protected ReferenceBean doBuild() {
        return new ReferenceBean<Object>();
    }

    @Override
    protected void preConfigureBean(DubboBeanSpec dubboBeanSpec, ReferenceBean referenceBean) {
        Assert.notNull(interfaceClass, "The interface class must set first!");

        // Bind annotation attributes
        List<String> ignoreFields = CollectionUtil.unmodifyList(IGNORE_FIELD_NAMES);

        Map<String, Field> referenceBeanFieldMap = ReflectUtils.getBeanPropertyFields(referenceBean.getClass());

        referenceBeanFieldMap.keySet().stream()
                .filter(f -> !ignoreFields.contains(f))
                .forEach(f -> {
                    AnnotationAttributes annotationAttributes = dubboBeanSpec.getAnnotationAttributes();
                    try {
                        Field field = ReferenceBean.class.getField(f);
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);

                        Object o = annotationAttributes.get(f);

                        field.set(referenceBean, o);
                        field.setAccessible(accessible);
                    } catch (Exception e) {
                        throw new SumkException(-345365, "ReferenceBeanBuilder preConfigureBean", e);
                    }
                });
    }

    @Override
    protected String resolveModuleConfigBeanName(DubboBeanSpec dubboBeanSpec) {
        return dubboBeanSpec.getAnnotationAttributes().getString("module");
    }

    @Override
    protected String resolveApplicationConfigBeanName(DubboBeanSpec dubboBeanSpec) {
        return dubboBeanSpec.getAnnotationAttributes().getString("application");
    }

    @Override
    protected String[] resolveRegistryConfigBeanNames(DubboBeanSpec dubboBeanSpec) {
        return dubboBeanSpec.getAnnotationAttributes().getStringArray("registry");
    }

    @Override
    protected String resolveMonitorConfigBeanName(DubboBeanSpec dubboBeanSpec) {
        return dubboBeanSpec.getAnnotationAttributes().getString("monitor");
    }

    @Override
    protected void postConfigureBean(DubboBeanSpec dubboBeanSpec, ReferenceBean bean) throws Exception {

        configureInterface(dubboBeanSpec, bean);

        configureConsumerConfig(dubboBeanSpec, bean);

        configureMethodConfig(dubboBeanSpec, bean);

        bean.afterPropertiesSet();

    }

    public static ReferenceBeanBuilder create(DubboBeanSpec dubboBeanSpec) {
        return new ReferenceBeanBuilder(dubboBeanSpec);
    }
}