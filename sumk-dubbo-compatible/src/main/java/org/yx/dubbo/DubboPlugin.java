package org.yx.dubbo;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.dubbo.bean.ServiceClassPostProcessor;
import org.yx.dubbo.config.DubboConfig;
import org.yx.dubbo.listener.DubboEventListener;
import org.yx.dubbo.listener.event.DubboCloseEvent;
import org.yx.dubbo.listener.event.DubboEventPublisher;
import org.yx.dubbo.listener.event.DubboStartEvent;
import org.yx.dubbo.main.DubboStartConstants;
import org.yx.main.StartContext;

import java.util.List;
import java.util.Objects;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class DubboPlugin implements Plugin {

    @Override
    public void startAsync() {
        if (StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO) == null
                || Objects.equals(StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO), false)) {
            return;
        }
        // 配置初始化
        DubboConfig.init();
        // 初始化provider
        ServiceClassPostProcessor.init();

        buildDubboListeners();
        DubboEventPublisher.publish(new DubboStartEvent(new Object()));
    }

    protected void buildDubboListeners() {
        List<DubboEventListener> listeners = IOC.getBeans(DubboEventListener.class);
        DubboEventPublisher.group().setListener(listeners.toArray(new DubboEventListener[0]));
    }

    @Override
    public void prepare() {
        // todo 找出所有dubbo注解注入smuk down
        // todo 配置变更监听 down
        // todo 创建容器销毁事件listener down
        // todo 加载Dubbo 注解
    }

    @Override
    public void afterStarted() {

    }

    @Override
    public void stop() {
        DubboEventPublisher.publish(new DubboCloseEvent(new Object()));
    }
}
