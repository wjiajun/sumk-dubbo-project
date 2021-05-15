package org.yx.dubbo.bean;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.annotation.Reference;

/**
 * @author : wjiajun
 * @description:
 */
public class ReferenceBean<T> extends ReferenceConfig<T> {

    public ReferenceBean() {
        super();
    }

    public ReferenceBean(Reference reference) {
        super(reference);
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

    @Override
    public boolean shouldInit() {
        Boolean shouldInit = isInit();
        if (shouldInit == null && getConsumer() != null) {
            shouldInit = getConsumer().isInit();
        }
        if (shouldInit == null) {
            // default is true, spring will still init lazily by setting init's default value to false,
            // the def default setting happens in {@link ReferenceBean#afterPropertiesSet}.
            return true;
        }
        return shouldInit;
    }
}
