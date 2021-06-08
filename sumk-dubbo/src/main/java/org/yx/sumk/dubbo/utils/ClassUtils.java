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

import org.apache.dubbo.common.utils.Assert;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author : wjiajun
 */
public class ClassUtils {

    /**
     * 获取class包含父类的所有接口
     * @param clazz
     * @param classLoader
     * @return
     */
    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, ClassLoader classLoader) {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface() && isVisible(clazz, classLoader)) {
            return Collections.<Class<?>>singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
        Class<?> current = clazz;
        while (current != null) {
            Class<?>[] ifcs = current.getInterfaces();
            for (Class<?> ifc : ifcs) {
                interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
            }
            current = current.getSuperclass();
        }
        return interfaces;
    }

    /**
     * 判断当前类是否对当前classLoader可见
     * @param clazz
     * @param classLoader
     * @return
     */
    public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
        if (classLoader == null) {
            return true;
        }
        try {
            Class<?> actualClass = classLoader.loadClass(clazz.getName());
            return (clazz == actualClass);
            // Else: different interface class found...
        }
        catch (ClassNotFoundException ex) {
            // No interface class found...
            return false;
        }
    }

}