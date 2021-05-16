package org.yx.provider.boostrap;

import org.yx.bean.IOC;
import org.yx.dubbo.DubboPlugin;
import org.yx.dubbo.main.DubboStartConstants;
import org.yx.log.Log;
import org.yx.log.LogType;
import org.yx.main.StartConstants;
import org.yx.main.SumkServer;
import org.yx.provider.service.DefaultDemoService;

/**
 * @author : wjiajun
 * @description:
 */
public class DubboProviderBoostrap {

    public static void main(String[] args) {
        Log.setLogType(LogType.slf4j);//因为没有引入日志包，才写的临时代码
        long begin=System.currentTimeMillis();
        SumkServer.start(StartConstants.NOHTTP, DubboStartConstants.ENABLE_DUBBO);
        System.out.println("启动完成,除zookeeper服务器外耗时："+(System.currentTimeMillis()-begin)+"毫秒");

        DubboPlugin demoBeanService = IOC.get(DubboPlugin.class);
        System.out.println();

        DefaultDemoService demoService = IOC.get(DefaultDemoService.class);
        System.out.println(demoService.sayHello("123"));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Log.printStack("main",e);
        }
    }
}
