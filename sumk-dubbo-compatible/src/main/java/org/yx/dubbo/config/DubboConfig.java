package org.yx.dubbo.config;

import org.apache.dubbo.common.config.Environment;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.yx.conf.AppConfig;
import org.yx.conf.AppInfo;
import org.yx.dubbo.main.DubboStartConstants;
import org.yx.log.Logs;
import org.yx.log.RawLog;
import org.yx.main.StartContext;
import org.yx.util.CollectionUtil;
import org.yx.util.IOUtil;
import org.yx.util.StringUtil;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Dubbo配置加载
 * @author : wjiajun
 * @description:
 */
public class DubboConfig {

    public static synchronized void init() {
        if (StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO) == null
                || Objects.equals(StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO), false)) {
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
