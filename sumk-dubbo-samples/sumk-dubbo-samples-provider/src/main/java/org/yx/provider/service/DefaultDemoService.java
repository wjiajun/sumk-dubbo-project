package org.yx.provider.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.yx.annotation.Inject;
import org.yx.consumer.DemoService;
import org.yx.provider.sumk.bean.DemoBeanService;

/**
 * @author : wjiajun
 * @description:
 */
@DubboService(version = "${demo.service.version}")
public class DefaultDemoService implements DemoService {

    @Inject
    private DemoBeanService demoBeanService;

    @Override
    public String sayHello(String name) {
        return demoBeanService.sayHello(name);
    }
}
