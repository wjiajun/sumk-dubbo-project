package org.yx.dubbo.utils;

import org.apache.dubbo.common.utils.ArrayUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.yx.conf.AppInfo;
import org.yx.dubbo.annotation.AnnotationAttributes;
import org.yx.dubbo.spec.DubboBeanSpec;

import java.util.HashMap;
import java.util.Map;


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
        StringBuilder beanNameBuilder = new StringBuilder("@Reference");
        AnnotationAttributes attributes = dubboBeanSpec.getAnnotationAttributes();
        if (!attributes.isEmpty()) {
            beanNameBuilder.append('(');
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                beanNameBuilder.append(entry.getKey())
                        .append('=')
                        .append(entry.getValue())
                        .append(',');
            }
            // replace the latest "," to be ")"
            beanNameBuilder.setCharAt(beanNameBuilder.lastIndexOf(","), ')');
        }

        beanNameBuilder.append(" ").append(dubboBeanSpec.getInterfaceClass().getName());
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

    public static Map<String, String> convertParameters(String[] parameters) {
        if (ArrayUtils.isEmpty(parameters)) {
            return null;
        }

        if (parameters.length % 2 != 0) {
            throw new IllegalArgumentException("parameter attribute must be paired with key followed by value");
        }

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < parameters.length; i += 2) {
            map.put(parameters[i], parameters[i + 1]);
        }
        return map;
    }
}
