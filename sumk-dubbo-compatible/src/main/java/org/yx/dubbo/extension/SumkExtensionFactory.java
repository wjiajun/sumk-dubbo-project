package org.yx.dubbo.extension;

import org.apache.dubbo.common.extension.ExtensionFactory;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.yx.bean.IOC;

/**
 * SumkExtensionFactory
 */
public class SumkExtensionFactory implements ExtensionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SumkExtensionFactory.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getExtension(Class<T> type, String name) {

        //SPI should be get from SpiExtensionFactory
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            return null;
        }

        // 获得属性
        T bean = IOC.get(name, type);
        // 判断类型
        if (bean != null) {
            return bean;
        }

        return null;
    }
}
