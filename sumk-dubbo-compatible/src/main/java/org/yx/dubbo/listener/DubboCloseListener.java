package org.yx.dubbo.listener;

import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.dubbo.listener.event.DubboCloseEvent;
import org.yx.dubbo.main.DubboStartConstants;
import org.yx.listener.SumkEvent;
import org.yx.log.Logs;
import org.yx.main.StartContext;

import java.util.Objects;

@Bean
public class DubboCloseListener implements DubboEventListener {

	private static final Logger logger = Logs.rpc();

	@Override
	public void listen(SumkEvent ev) {
		if (StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO) == null
				|| Objects.equals(StartContext.inst().get(DubboStartConstants.ENABLE_DUBBO), false)) {
			return;
		}

		if(ev instanceof DubboCloseEvent) {
			logger.info("dubbo plugin dubboBootstrap is closing....");
			DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance();
			dubboBootstrap.stop();
			logger.info("dubbo plugin dubboBootstrap been closed....");
		}
	}

}