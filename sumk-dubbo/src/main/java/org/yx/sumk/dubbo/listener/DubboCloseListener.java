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

package org.yx.sumk.dubbo.listener;

import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.sumk.dubbo.config.DubboConst;
import org.yx.sumk.dubbo.listener.event.DubboCloseEvent;
import org.yx.listener.SumkEvent;
import org.yx.log.Logs;
import org.yx.main.StartContext;

import java.util.Objects;

/**
 * @author wjiajun
 */
@Bean
public class DubboCloseListener implements DubboEventListener {

	private static final Logger logger = Logs.rpc();

	@Override
	public void listen(SumkEvent ev) {
		if (StartContext.inst().get(DubboConst.ENABLE_DUBBO) == null
				|| Objects.equals(StartContext.inst().get(DubboConst.ENABLE_DUBBO), false)) {
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