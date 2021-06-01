package org.yx.consumer.boostrap;

import org.yx.bean.IOC;
import org.yx.consumer.boostrap.service.DefaultConsumerDemoService;
import org.yx.consumer.boostrap.service.SumkTraceConsumerService;
import org.yx.dubbo.config.DubboConst;
import org.yx.log.Log;
import org.yx.log.LogType;
import org.yx.main.StartConstants;
import org.yx.main.SumkServer;

/**
 * @author : wjiajun
 * @description:
 */
public class DubboConsumerBoostrap {

    public static void main(String[] args) {
        Log.setLogType(LogType.slf4j);
        long begin=System.currentTimeMillis();
        SumkServer.start(StartConstants.NOHTTP, DubboConst.ENABLE_DUBBO);
        System.out.println("启动完成,除zookeeper服务器外耗时："+(System.currentTimeMillis()-begin)+"毫秒");

        // 验证injvm
        DefaultConsumerDemoService demoService = IOC.get(DefaultConsumerDemoService.class);
        System.out.println(demoService.run());

        // 验证trace
        SumkTraceConsumerService sumkTraceConsumerService = IOC.get(SumkTraceConsumerService.class);
        sumkTraceConsumerService.testTrace();;

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Log.printStack("main",e);
        }
    }
}
