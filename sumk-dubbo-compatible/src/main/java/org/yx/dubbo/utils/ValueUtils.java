package org.yx.dubbo.utils;

import org.yx.conf.AppInfo;

import java.util.StringJoiner;

/**
 * @author : wjiajun
 * @description:
 */
public class ValueUtils {

    public static String getValue(String value) {
        String replace = value.replace("${", "").replace("}", "");
        return AppInfo.getLatin(replace, "");
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
