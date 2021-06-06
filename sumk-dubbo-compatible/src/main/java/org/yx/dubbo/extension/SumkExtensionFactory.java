package org.yx.dubbo.extension;

import org.apache.dubbo.common.extension.ExtensionFactory;
import org.apache.dubbo.common.extension.SPI;
import org.yx.bean.IOC;

/**
 * @author wjiajun
 * SumkExtensionFactory
 */
public class SumkExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {

        //SPI should be get from SpiExtensionFactory
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            return null;
        }

        return IOC.get(name, type);
    }
}
