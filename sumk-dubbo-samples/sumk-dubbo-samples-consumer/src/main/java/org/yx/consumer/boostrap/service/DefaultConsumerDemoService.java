package org.yx.consumer.boostrap.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.yx.annotation.Bean;
import org.yx.common.context.ActionContext;
import org.yx.common.context.LogContext;
import org.yx.consumer.DemoService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class DefaultConsumerDemoService {

//    @DubboReference(version = "${demo.service.version}", url = "${demo.service.url}")
    @DubboReference(version = "1.0.0")
    private DemoService demoService;

    public String run() {
        return demoService.sayHello("consumer");
    }
}
