package org.yx.consumer.boostrap;

import org.yx.bean.IOC;
import org.yx.consumer.boostrap.service.DefaultConsumerDemoService;
import org.yx.dubbo.main.DubboStartConstants;
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
        Log.setLogType(LogType.slf4j);//因为没有引入日志包，才写的临时代码
        long begin=System.currentTimeMillis();
        SumkServer.start(StartConstants.NOHTTP, DubboStartConstants.ENABLE_DUBBO);
        System.out.println("启动完成,除zookeeper服务器外耗时："+(System.currentTimeMillis()-begin)+"毫秒");

        DefaultConsumerDemoService demoService = IOC.get(DefaultConsumerDemoService.class);
        System.out.println(demoService.run());

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Log.printStack("main",e);
        }
    }
}
