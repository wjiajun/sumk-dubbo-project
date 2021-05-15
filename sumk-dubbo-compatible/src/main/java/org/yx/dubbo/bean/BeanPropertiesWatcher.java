package org.yx.dubbo.bean;

import org.yx.annotation.Bean;
import org.yx.annotation.spec.parse.SpecParsers;
import org.yx.bean.Loader;
import org.yx.bean.watcher.BeanInjectWatcher;
import org.yx.dubbo.spec.DubboBeanSpec;
import org.yx.dubbo.spec.DubboBuiltIn;
import org.yx.dubbo.utils.ResolveUtils;
import org.yx.exception.SumkException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class BeanPropertiesWatcher implements BeanInjectWatcher {

    @Override
    public void afterInject(List<Object> beans) {
//        beans.forEach(bean -> {
//            // 配置sumk bean中包含@Reference的属性
//            try {
//                ReferenceBeanPostProcessor.injectProperties(bean);
//            } catch (Exception e) {
//                throw new SumkException(-345365, "IOC Reference error on " + bean, e);
//            }
//        });
    }

}
