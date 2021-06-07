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

package org.yx.provider.boostrap;

import org.yx.dubbo.config.DubboConst;
import org.yx.log.Log;
import org.yx.log.LogType;
import org.yx.main.StartConstants;
import org.yx.main.SumkServer;

/**
 * @author : wjiajun
 */
public class DubboProviderBoostrap {

    public static void main(String[] args) {
        Log.setLogType(LogType.slf4j);
        long begin=System.currentTimeMillis();
        SumkServer.start(StartConstants.NOHTTP, DubboConst.ENABLE_DUBBO);
        System.out.println("启动完成,除zookeeper服务器外耗时："+(System.currentTimeMillis()-begin)+"毫秒");

//        DubboPlugin demoBeanService = IOC.get(DubboPlugin.class);
//        System.out.println();

//        DefaultDemoService demoService = IOC.get(DefaultDemoService.class);
//        System.out.println(demoService.sayHello("123"));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Log.printStack("main",e);
        }
    }
}