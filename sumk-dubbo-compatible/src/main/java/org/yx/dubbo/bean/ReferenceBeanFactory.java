package org.yx.dubbo.bean;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;

import java.lang.annotation.Annotation;

public final class ReferenceBeanFactory {

    public static ReferenceBean<?> create(Annotation referenceSpec) {
        if (DubboReference.class == referenceSpec.annotationType()) {
            DubboReference tmp = (DubboReference) referenceSpec;
            return new ReferenceBean<>(tmp);
        }

        return new ReferenceBean<>();
    }
}