package org.yx.dubbo;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.dubbo.bean.ReferenceBeanPostProcessor;
import org.yx.dubbo.bean.ServiceClassPostProcessor;
import org.yx.dubbo.config.DubboConfig;
import org.yx.dubbo.config.DubboConst;
import org.yx.dubbo.listener.DubboEventListener;
import org.yx.dubbo.listener.event.DubboCloseEvent;
import org.yx.dubbo.listener.event.DubboEventPublisher;
import org.yx.dubbo.listener.event.DubboStartEvent;
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
