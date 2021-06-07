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

package org.yx.dubbo.config;

import org.apache.dubbo.common.config.Environment;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.main.StartContext;

import java.util.Objects;

/**
 * Dubbo配置加载
 * @author : wjiajun
 */
public class DubboConfig {

    public static synchronized void init() {
        if (StartContext.inst().get(DubboConst.ENABLE_DUBBO) == null
                || Objects.equals(StartContext.inst().get(DubboConst.ENABLE_DUBBO), false)) {
            return;
        }
        AppInfo.addObserver(info -> {
            try {
                // dubbo app外部配置定时刷新（非配置中心）
                DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance();// 确保启动类初始化

                ConfigManager configManager = ApplicationModel.getConfigManager();
                Environment environment = ApplicationModel.getEnvironment();

                environment.updateAppExternalConfigurationMap(AppInfo.subMap(StringUtils.EMPTY_STRING));

                // 首次不需要refresh
                if(dubboBootstrap.isStarted()) {
                    configManager.refreshAll();
                }

            } catch (Exception e) {
                Logs.rpc().info(e.getMessage(), e);
            }
        });
    }

}