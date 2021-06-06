package org.yx.dubbo.bean;

import org.apache.dubbo.config.annotation.DubboService;

import java.lang.annotation.Annotation;

/**
 * @author wjiajun
 */
public final class ServiceBeanFactory {

    public static ServiceBean<Object> create(Annotation serviceSpec) {
        if (DubboService.class == serviceSpec.annotationType()) {
            DubboService service = (DubboService) serviceSpec;
            return new ServiceBean<>(service);
        }
        return new ServiceBean<>();
    }
}