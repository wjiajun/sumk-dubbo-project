package org.yx.dubbo.bean;

import org.yx.annotation.Bean;
import org.yx.bean.watcher.BeanInjectWatcher;

import java.util.List;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class BeanPropertiesWatcher implements BeanInjectWatcher {

    @Override
    public void afterInject(List<Object> beans) {

    }
}
