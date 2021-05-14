package org.yx.dubbo.ioc;

import org.yx.bean.IOC;
import org.yx.bean.InnerIOC;
import org.yx.exception.SumkException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wjiajun
 */
public class BeanRegistry {

    public static <T> T putSameBeanIfAlias(String name, Class<T> clz) {
        T bean = IOC.get(clz);
        if (bean != null) {
            return InnerIOC.putBean(name, bean);
        }
        try {
            return InnerIOC.putClass(name, clz);
        } catch (Exception e) {
            throw new SumkException(-345365, "IOC error on " + clz, e);
        }
    }

    public static <T> List<T> getBeans(String[] names, Class<T> clz) {
        return Arrays.stream(names)
                .map(name -> IOC.get(name, clz))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}