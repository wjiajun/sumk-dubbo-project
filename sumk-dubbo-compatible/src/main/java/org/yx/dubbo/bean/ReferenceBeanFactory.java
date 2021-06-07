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

package org.yx.dubbo.bean;

import org.apache.dubbo.config.annotation.DubboReference;

import java.lang.annotation.Annotation;

/**
 * @author wjiajun
 */
public final class ReferenceBeanFactory {

    public static ReferenceBean<?> create(Annotation referenceSpec) {
        if (DubboReference.class == referenceSpec.annotationType()) {
            DubboReference tmp = (DubboReference) referenceSpec;
            return new ReferenceBean<>(tmp);
        }

        return new ReferenceBean<>();
    }
}