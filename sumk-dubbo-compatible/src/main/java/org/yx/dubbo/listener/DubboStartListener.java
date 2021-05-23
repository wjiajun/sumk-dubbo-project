package org.yx.dubbo.listener;

import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.dubbo.config.DubboConst;
import org.yx.dubbo.listener.event.DubboStartEvent;
import org.yx.listener.SumkEvent;
import org.yx.log.Logs;
import org.yx.main.StartContext;

import java.util.Objects;

@Bean
public class DubboStartListener implements DubboEventListener {

	private static final Logger logger = Logs.rpc();

	@Override
	public void listen(SumkEvent ev) {
		if (StartContext.inst().get(DubboConst.ENABLE_DUBBO) == null
				|| Objects.equals(StartContext.inst().get(DubboConst.ENABLE_DUBBO), false)) {
			return;
		}

		if(ev instanceof DubboStartEvent) {
			logger.info("dubbo plugin dubboBootstrap is opening....");
			DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance();
			dubboBootstrap.start();
			logger.info("dubbo plugin dubboBootstrap been open....");
		}
	}

}