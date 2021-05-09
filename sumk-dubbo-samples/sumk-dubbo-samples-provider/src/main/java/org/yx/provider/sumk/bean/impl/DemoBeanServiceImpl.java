package org.yx.provider.sumk.bean.impl;

import org.yx.annotation.Bean;
import org.yx.provider.sumk.bean.DemoBeanService;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class DemoBeanServiceImpl implements DemoBeanService {

    @Override
    public String sayHello(String name) {
        return "123: " + name;
    }
}
