/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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