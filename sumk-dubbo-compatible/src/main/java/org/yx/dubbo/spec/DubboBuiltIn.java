package org.yx.dubbo.spec;

import com.google.common.base.MoreObjects;
import org.apache.dubbo.common.utils.AnnotationUtils;
import org.apache.dubbo.common.utils.ServiceAnnotationResolver;
import org.yx.dubbo.annotation.AnnotationAttributes;
import org.yx.dubbo.annotation.AnnotationResolver;
import org.yx.dubbo.annotation.ReferenceAnnotationResolver;
import org.yx.dubbo.utils.ValueUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author : wjiajun
 * @description:
 */
public class DubboBuiltIn {

    private final static List<Class<? extends Annotation>> serviceAnnotationTypes = ServiceAnnotationResolver.SERVICE_ANNOTATION_CLASSES;

    private final static List<Class<? extends Annotation>> referenceAnnotationTypes = ReferenceAnnotationResolver.REFERENCE_ANNOTATION_CLASSES;

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

        String group = serviceAnnotationResolver.resolveGroup();
        String version = ValueUtils.getValue(serviceAnnotationResolver.resolveVersion());

        AnnotationAttributes annotationAttributes = AnnotationResolver.getAnnotationAttributes(dubboService);
        Class<?> interfaceClass = AnnotationResolver.resolveServiceInterfaceClass(annotationAttributes, clz);
        String interfaceName = MoreObjects.firstNonNull(interfaceClass.getName(), serviceAnnotationResolver.resolveInterfaceClassName());

        String application = AnnotationUtils.getAttribute(dubboService, "application");

        return new DubboBeanSpec(interfaceClass, interfaceName, version, group, application, annotationAttributes);
    };

    public static final BiFunction<Object, Field, DubboBeanSpec> DUBBO_REFERENCE_PARSER = (src, f) -> {
        boolean isDubboService = referenceAnnotationTypes.stream().anyMatch(t -> f.getAnnotation(t) != null);
        if (!isDubboService) {
            return null;
        }

        Class<?> clz = f.getType();
        ReferenceAnnotationResolver referenceAnnotationResolver = new ReferenceAnnotationResolver(clz, f);
        Annotation dubboReference = referenceAnnotationResolver.getReferenceAnnotation();

        if (dubboReference == null) {
            return null;
        }

        String group = referenceAnnotationResolver.resolveGroup();
        String version = ValueUtils.getValue(referenceAnnotationResolver.resolveVersion());

        AnnotationAttributes annotationAttributes = AnnotationResolver.getAnnotationAttributes(dubboReference);
        Class<?> interfaceClass = AnnotationResolver.resolveServiceInterfaceClass(annotationAttributes, clz);
        String interfaceName = MoreObjects.firstNonNull(interfaceClass.getName(), referenceAnnotationResolver.resolveInterfaceClassName());

        String application = AnnotationUtils.getAttribute(dubboReference, "application");

        return new DubboBeanSpec(interfaceClass, interfaceName, version, group, application, annotationAttributes);
    };
}
