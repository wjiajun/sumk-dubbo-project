package org.yx.dubbo.config;

import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.yx.conf.AppInfo;
import org.yx.dubbo.main.DubboStartConstants;
import org.yx.log.Logs;
import org.yx.main.StartContext;

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
                // application
                Map<String, String> applicationConfig = AppInfo.subMap(DubboConst.DEFAULT_NAME + "." + DubboConst.DEFAULT_APPLICATION_CONFIG_NAME + ".");
                // register
                Map<String, String> registerConfig = AppInfo.subMap(DubboConst.DEFAULT_NAME + "." + DubboConst.DEFAULT_REGISTRY_CONFIG_NAME + ".");

                registerApplicationConfig(applicationConfig);
                registerRegistryConfig(registerConfig);
            } catch (Exception e) {
                Logs.rpc().info(e.getMessage(), e);
            }
        });
    }

    private static void registerApplicationConfig(Map<String, String> applicationConfig) {
        String applicationName = applicationConfig.getOrDefault("name", "application");
        DubboBootstrap.getInstance().application(applicationName, (t) -> {
            t.name(applicationName);
        });
    }

    private static void registerRegistryConfig(Map<String, String> applicationConfig) {
        String address = applicationConfig.getOrDefault("address", "N/A");// 默认不注册
        DubboBootstrap.getInstance().registry((t) -> {
            t.address(address);
        });
    }
}
