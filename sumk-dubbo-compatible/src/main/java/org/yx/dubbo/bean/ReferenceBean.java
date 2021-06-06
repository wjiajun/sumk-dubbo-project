package org.yx.dubbo.bean;

import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.annotation.DubboReference;

/**
 * @author : wjiajun
 * @description:
 */
public class ReferenceBean<T> extends ReferenceConfig<T> {

    public ReferenceBean() {
        super();
    }

    public ReferenceBean(DubboReference reference) {
        super();
        appendAnnotation(DubboReference.class, reference);
        setMethods(MethodConfig.constructMethodConfig(reference.methods()));
    }

    public void afterPropertiesSet() {
        // lazy init by default.
        if (init == null) {
            init = false;
        }

        if (shouldInit()) {
            get();
        }
    }

}
