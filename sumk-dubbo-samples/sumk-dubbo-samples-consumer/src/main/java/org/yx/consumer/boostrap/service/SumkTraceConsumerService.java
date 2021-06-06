package org.yx.consumer.boostrap.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.yx.annotation.Bean;
import org.yx.common.context.ActionContext;
import org.yx.common.context.LogContext;
import org.yx.consumer.SumkTraceService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wjiajun
 */
@Bean
public class SumkTraceConsumerService {

    @DubboReference(version = "1.0.0", injvm = false)
    private SumkTraceService traceService;

    public void testTrace() {
        Map<String, String> param = new HashMap();
        param.put("key1", "value1");
        LogContext logContext = LogContext.create("act", "traceId1", "spanId1", "userId1", true, param);
        ActionContext.newContext(logContext);

        System.out.println("traceId:" + ActionContext.current().traceId());
        System.out.println("spanId:" + ActionContext.current().spanId());

        System.out.println(traceService.echo("123"));
    }
}
