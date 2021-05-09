package org.yx.dubbo.spec;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;

/**
 * @author : wjiajun
 * @description:
 */
public class DubboBuiltIn {

    private final static List<Class<? extends Annotation>> serviceAnnotationTypes = asList(
            DubboService.class,
            Service.class,
            com.alibaba.dubbo.config.annotation.Service.class
    );

    public static final Function<Class<?>, DubboBeanSpec> DUBBO_PARSER = clz -> {
        boolean isDubboService = serviceAnnotationTypes.stream().anyMatch(t -> clz.getAnnotation(t) != null);
        if(!isDubboService) {
            return null;
        }
        for (int i = 0; i < serviceAnnotationTypes.size(); i++) {
            Annotation dubboService = clz.getAnnotation(serviceAnnotationTypes.get(i));
            if (dubboService == null) {
                continue;
            }

            if (DubboService.class == dubboService.annotationType()) {
                DubboService tmp = (DubboService) dubboService;
                return new DubboBeanSpec(tmp.interfaceClass(), tmp.interfaceName(), tmp.version(), tmp.group(), tmp.application());
            }

            if (Service.class == dubboService.annotationType()) {
                Service tmp = (Service) dubboService;
                return new DubboBeanSpec(tmp.interfaceClass(), tmp.interfaceName(), tmp.version(), tmp.group(), tmp.application());
            }

            if (com.alibaba.dubbo.config.annotation.Service.class == dubboService.annotationType()) {
                com.alibaba.dubbo.config.annotation.Service tmp = (com.alibaba.dubbo.config.annotation.Service) dubboService;
                return new DubboBeanSpec(tmp.interfaceClass(), tmp.interfaceName(), tmp.version(), tmp.group(), tmp.application());
            }
        }
        return null;
    };
}
