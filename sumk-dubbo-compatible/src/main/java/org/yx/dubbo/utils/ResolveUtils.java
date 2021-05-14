package org.yx.dubbo.utils;

import org.apache.dubbo.common.utils.StringUtils;
import org.yx.conf.AppInfo;
import org.yx.dubbo.spec.DubboBeanSpec;


/**
 * @author : wjiajun
 * @description:
 */
public class ResolveUtils {

    public static String generateServiceBeanName(DubboBeanSpec dubboBeanSpec) {
        StringBuilder beanNameBuilder = new StringBuilder("ServiceBean");
        // Required
        append(beanNameBuilder, dubboBeanSpec.getInterfaceName());
        // Optional
        append(beanNameBuilder, dubboBeanSpec.getVersion());
        append(beanNameBuilder, dubboBeanSpec.getGroup());
        // Build and remove last ":"
        return beanNameBuilder.toString();
    }

    public static String generateReferenceBeanName(DubboBeanSpec dubboBeanSpec) {
        // todo 需增加额外参数区分
        StringBuilder beanNameBuilder = new StringBuilder("@Reference");
        // Required
        append(beanNameBuilder, dubboBeanSpec.getInterfaceName());
        // Optional
        append(beanNameBuilder, dubboBeanSpec.getVersion());
        append(beanNameBuilder, dubboBeanSpec.getGroup());
        // Build and remove last ":"
        return beanNameBuilder.toString();
    }

    private static void append(StringBuilder builder, String value) {
        if (StringUtils.isNoneEmpty(value)) {
            if (value.contains("${")) {
                String replace = value.replace("${", "").replace("}", "");
                builder.append(":").append(AppInfo.getLatin(replace, ""));
                return;
            }
            builder.append(":").append(value);
        }
    }
}
