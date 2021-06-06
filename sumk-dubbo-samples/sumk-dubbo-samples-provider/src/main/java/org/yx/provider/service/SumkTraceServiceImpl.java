package org.yx.provider.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.yx.common.context.ActionContext;
import org.yx.consumer.SumkTraceService;

/**
 * @author : wjiajun
 * @description:
 */
@DubboService(version = "1.0.0")
public class SumkTraceServiceImpl implements SumkTraceService {

    @Override
    public String echo(String s){
        System.out.println(ActionContext.current().userId());
        System.out.println(ActionContext.current().getAttachment("key1"));
        System.out.println("traceId:" + ActionContext.current().traceId());
        System.out.println("spanId:" + ActionContext.current().spanId());
        return s;
    }
}
