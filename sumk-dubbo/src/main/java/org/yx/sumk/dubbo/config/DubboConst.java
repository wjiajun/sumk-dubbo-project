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

package org.yx.sumk.dubbo.config;

/**
 * @author : wjiajun
 */
public class DubboConst {

    public static final String ENABLE_DUBBO = DubboConst.DEFAULT_NAME;

    public static final String DEFAULT_NAME = "META-INF/dubbo";

    public static final String DEFAULT_APPLICATION_CONFIG_NAME = "application";

    public static final String DEFAULT_REGISTRY_CONFIG_NAME = "registry";

    public static final String DEFAULT_PROTOCOL_CONFIG_NAME = "protocol";

    public static final String DEFAULT_SERVICE_CONFIG_NAME = "service";

    public static final String DEFAULT_REFERENCE_CONFIG_NAME = "reference";

    public static final String DEFAULT_PROVIDER_CONFIG_NAME = "provider";

    public static final String DEFAULT_CONSUMER_CONFIG_NAME = "consumer";

    public static final String SUMK_CONFIG_PREFIX = "${";

    public static final String SUMK_CONFIG_SUFFIX = "}";
}