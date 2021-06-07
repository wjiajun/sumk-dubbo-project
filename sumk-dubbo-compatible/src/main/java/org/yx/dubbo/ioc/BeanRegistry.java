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