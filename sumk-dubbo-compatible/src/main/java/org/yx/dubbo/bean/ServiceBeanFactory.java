package org.yx.dubbo.bean;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;

import java.lang.annotation.Annotation;

public final class ServiceBeanFactory {

    public static ServiceBean create(Annotation serviceSpec) {
        if (Service.class == serviceSpec.annotationType()) {
            Service service = (Service) serviceSpec;
            return new ServiceBean<>(service);
        }

        if (DubboService.class == serviceSpec.annotationType()) {
            DubboService service = (DubboService) serviceSpec;
            return new ServiceBean<>(service);
        }

        if (com.alibaba.dubbo.config.annotation.Service.class == serviceSpec.annotationType()) {
            com.alibaba.dubbo.config.annotation.Service service = (com.alibaba.dubbo.config.annotation.Service) serviceSpec;
            return new ServiceBean<>(service);
        }
        return new ServiceBean<>();
    }
}