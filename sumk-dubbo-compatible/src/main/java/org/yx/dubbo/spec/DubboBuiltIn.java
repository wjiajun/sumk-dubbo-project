package org.yx.dubbo.spec;

import org.apache.dubbo.common.utils.AnnotationUtils;
import org.apache.dubbo.common.utils.ServiceAnnotationResolver;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.yx.dubbo.annotation.AnnotationAttributes;
import org.yx.dubbo.annotation.AnnotationResolver;
import org.yx.dubbo.utils.ResolveUtils;
import org.yx.dubbo.utils.ValueUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Arrays.asList;

/**
 * @author : wjiajun
 * @description:
 */
public class DubboBuiltIn {

    private final static List<Class<? extends Annotation>> serviceAnnotationTypes = ServiceAnnotationResolver.SERVICE_ANNOTATION_CLASSES;

    private final static List<Class<? extends Annotation>> referenceAnnotationTypes = asList(
            DubboReference.class,
            Reference.class,
            com.alibaba.dubbo.config.annotation.Reference.class
    );

    public static final Function<Class<?>, DubboBeanSpec> DUBBO_PARSER = clz -> {
        boolean isDubboService = serviceAnnotationTypes.stream().anyMatch(t -> clz.getAnnotation(t) != null);
        if (!isDubboService) {
            return null;
        }

        ServiceAnnotationResolver serviceAnnotationResolver = new ServiceAnnotationResolver(clz);
        Annotation dubboService = serviceAnnotationResolver.getServiceAnnotation();

        if (dubboService == null) {
            return null;
        }

        String interfaceName = serviceAnnotationResolver.resolveInterfaceClassName();
        String group = serviceAnnotationResolver.resolveGroup();
        String version = ValueUtils.getValue(serviceAnnotationResolver.resolveVersion());

        AnnotationAttributes annotationAttributes = AnnotationResolver.getAnnotationAttributes(dubboService);
        Class<?> interfaceClass = AnnotationResolver.resolveServiceInterfaceClass(annotationAttributes, clz);
        String application = AnnotationUtils.getAttribute(dubboService, "application");

        return new DubboBeanSpec(interfaceClass, interfaceName, version, group, application, annotationAttributes);
    };

    public static final BiFunction<Object, Field, DubboBeanSpec> DUBBO_REFERENCE_PARSER = (src, f) -> {
        boolean isDubboService = referenceAnnotationTypes.stream().anyMatch(t -> f.getClass().getAnnotation(t) != null);
        if (!isDubboService) {
            return null;
        }

        for (int i = 0; i < referenceAnnotationTypes.size(); i++) {
            Annotation dubboService = f.getAnnotation(referenceAnnotationTypes.get(i));
            if (dubboService == null) {
                continue;
            }

            if (DubboReference.class == dubboService.annotationType()) {
                DubboReference tmp = (DubboReference) dubboService;
                return new DubboBeanSpec(tmp.interfaceClass(), tmp.interfaceName(), tmp.version(), tmp.group(),
                        tmp.application(), AnnotationResolver.getAnnotationAttributes(dubboService));
            }

            if (Reference.class == dubboService.annotationType()) {
                Reference tmp = (Reference) dubboService;
                return new DubboBeanSpec(tmp.interfaceClass(), tmp.interfaceName(), tmp.version(), tmp.group(),
                        tmp.application(), AnnotationResolver.getAnnotationAttributes(dubboService));
            }

            if (com.alibaba.dubbo.config.annotation.Reference.class == dubboService.annotationType()) {
                com.alibaba.dubbo.config.annotation.Reference tmp = (com.alibaba.dubbo.config.annotation.Reference) dubboService;
                return new DubboBeanSpec(tmp.interfaceClass(), tmp.interfaceName(), tmp.version(), tmp.group(), tmp.application(),
                        AnnotationResolver.getAnnotationAttributes(dubboService));
            }
        }
        return null;

    };
}
