package org.yx.consumer.boostrap.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.yx.annotation.Bean;
import org.yx.consumer.DemoService;

/**
 * @author : wjiajun
 */
@Bean
public class DefaultConsumerDemoService {

    @DubboReference(version = "1.0.0")
    private DemoService demoService;

    public String run() {
        return demoService.sayHello("consumer");
    }
}
