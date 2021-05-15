package org.yx.consumer.boostrap.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.yx.annotation.Bean;
import org.yx.consumer.DemoService;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class DefaultConsumerDemoService {

//    @DubboReference(version = "${demo.service.version}", url = "${demo.service.url}")
    @DubboReference(version = "${demo.service.version}")
    private DemoService demoService;

    public String run() {
        return demoService.sayHello("consumer");
    }
}
