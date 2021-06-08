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

package org.yx.sumk.dubbo;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.sumk.dubbo.bean.ReferenceBeanPostProcessor;
import org.yx.sumk.dubbo.bean.ServiceClassPostProcessor;
import org.yx.sumk.dubbo.config.DubboConfig;
import org.yx.sumk.dubbo.config.DubboConst;
import org.yx.sumk.dubbo.listener.DubboEventListener;
import org.yx.sumk.dubbo.listener.event.DubboCloseEvent;
import org.yx.sumk.dubbo.listener.event.DubboEventPublisher;
import org.yx.sumk.dubbo.listener.event.DubboStartEvent;
import org.yx.main.StartContext;

import java.util.List;
import java.util.Objects;

/**
 * @author : wjiajun
 */
@Bean
public class DubboPlugin implements Plugin {

    @Override
    public void startAsync() {
        if (StartContext.inst().get(DubboConst.ENABLE_DUBBO) == null
                || Objects.equals(StartContext.inst().get(DubboConst.ENABLE_DUBBO), false)) {
            return;
        }
        // 配置初始化
        DubboConfig.init();
        // 初始化provider
        ServiceClassPostProcessor.init();
        // 初始化reference
        ReferenceBeanPostProcessor.init();

        buildDubboListeners();
        DubboEventPublisher.publish(new DubboStartEvent(new Object()));
    }

    protected void buildDubboListeners() {
        List<DubboEventListener> listeners = IOC.getBeans(DubboEventListener.class);
        DubboEventPublisher.group().setListener(listeners.toArray(new DubboEventListener[0]));
    }

    @Override
    public void prepare() {
    }

    @Override
    public void afterStarted() {
    }

    @Override
    public void stop() {
        DubboEventPublisher.publish(new DubboCloseEvent(new Object()));
    }
}