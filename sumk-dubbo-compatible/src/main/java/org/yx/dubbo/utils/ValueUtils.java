package org.yx.dubbo.utils;

import org.apache.dubbo.common.utils.StringUtils;
import org.yx.conf.AppInfo;
import org.yx.dubbo.config.DubboConst;

import java.util.StringJoiner;

/**
 * @author : wjiajun
 */
public class ValueUtils {

    public static String getValue(String value) {
        String replace = value.replace(DubboConst.SUMK_CONFIG_PREFIX, StringUtils.EMPTY_STRING).replace(DubboConst.SUMK_CONFIG_SUFFIX, StringUtils.EMPTY_STRING);
        return AppInfo.getLatin(replace, StringUtils.EMPTY_STRING);
    }

    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (ObjectUtils.isEmpty(arr)) {
            return "";
        }
        if (arr.length == 1) {
            return ObjectUtils.nullSafeToString(arr[0]);
        }

        StringJoiner sj = new StringJoiner(delim);
        for (Object o : arr) {
            sj.add(String.valueOf(o));
        }
        return sj.toString();
    }
}
