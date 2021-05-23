package org.yx.provider.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.yx.annotation.Inject;
import org.yx.common.context.ActionContext;
import org.yx.consumer.DemoService;
import org.yx.provider.sumk.bean.DemoBeanService;

/**
 * @author : wjiajun
 */
@DubboService(version = "1.0.0")
public class DefaultDemoService implements DemoService {

    @Inject
    private DemoBeanService demoBeanService;

    @Override
    public String sayHello(String name) {
        System.out.println(ActionContext.current().userId());
        System.out.println(ActionContext.current().getAttachment("key1"));
        System.out.println("traceId:" + ActionContext.current().traceId());
        System.out.println("spanId:" + ActionContext.current().spanId());
        return demoBeanService.sayHello(name);
    }
}
