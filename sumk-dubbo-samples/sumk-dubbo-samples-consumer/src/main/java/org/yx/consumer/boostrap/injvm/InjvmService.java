package org.yx.consumer.boostrap.injvm;

import org.apache.dubbo.config.annotation.DubboService;
import org.yx.consumer.DemoService;

/**
 * @author : wjiajun
 */
@DubboService(version = "1.0.0")
public class InjvmService implements DemoService {

    @Override
    public String sayHello(String name) {
        return "testInjvm";
    }
}
