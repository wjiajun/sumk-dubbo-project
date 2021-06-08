/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yx.sumk.dubbo.utils;

import org.apache.dubbo.common.utils.StringUtils;
import org.yx.conf.AppInfo;
import org.yx.sumk.dubbo.config.DubboConst;

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