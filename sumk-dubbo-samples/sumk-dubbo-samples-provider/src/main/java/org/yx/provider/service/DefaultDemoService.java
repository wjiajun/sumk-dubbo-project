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