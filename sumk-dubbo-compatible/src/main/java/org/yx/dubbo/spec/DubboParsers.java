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

package org.yx.dubbo.spec;

import org.yx.annotation.Bean;
import org.yx.annotation.Inject;
import org.yx.annotation.spec.BeanSpec;
import org.yx.annotation.spec.InjectSpec;

import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author : wjiajun
 */
public class DubboParsers {

    public static final Function<Class<?>, BeanSpec> BEAN_PARSER = clz -> {
        Bean c = clz.getAnnotation(Bean.class);
        if (c == null) {
            return null;
        }

        return new BeanSpec(c.value());
    };

    public static final BiFunction<Object, Field, InjectSpec> INJECT_PARSER = (src, f) -> {
        Inject c = f.getAnnotation(Inject.class);
        if (c == null) {
            return null;
        }

        return new InjectSpec(c.allowEmpty());
    };
}