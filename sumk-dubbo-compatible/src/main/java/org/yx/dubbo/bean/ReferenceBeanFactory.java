package org.yx.dubbo.bean;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;

import java.lang.annotation.Annotation;

public final class ReferenceBeanFactory {

    public static ReferenceBean create(Annotation referenceSpec) {
        if (Reference.class == referenceSpec.annotationType()) {
            Reference tmp = (Reference) referenceSpec;
            return new ReferenceBean<>(tmp);
        }

        if (DubboReference.class == referenceSpec.annotationType()) {
            DubboReference tmp = (DubboReference) referenceSpec;
            return new ReferenceBean<>(tmp);
        }

        if (com.alibaba.dubbo.config.annotation.Reference.class == referenceSpec.annotationType()) {
            com.alibaba.dubbo.config.annotation.Reference tmp = (com.alibaba.dubbo.config.annotation.Reference) referenceSpec;
            return new ReferenceBean<>(tmp);
        }
        return new ReferenceBean<>();
    }
}