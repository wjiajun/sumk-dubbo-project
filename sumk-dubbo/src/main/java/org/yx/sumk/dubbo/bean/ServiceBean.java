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

package org.yx.sumk.dubbo.bean;

import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.support.Parameter;

/**
 * @author : wjiajun
 */
public class ServiceBean<T> extends ServiceConfig<T> {

    private static final long serialVersionUID = 213195494150089726L;

    private final transient DubboService dubboService;

    private transient String beanName;

    public ServiceBean() {
        super();
        this.dubboService = null;
    }

    public ServiceBean(DubboService service) {
        super();
        this.dubboService = service;
        appendAnnotation(DubboService.class, service);
        setMethods(MethodConfig.constructMethodConfig(service.methods()));
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }

    public DubboService getService() {
        return dubboService;
    }

    public void afterPropertiesSet() {
        if (StringUtils.isEmpty(getPath())) {
            if (StringUtils.isNotEmpty(getInterface())) {
                setPath(getInterface());
            }
        }
    }

    @Parameter(excluded = true)
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void exported() {
        super.exported();
        // Publish ServiceBeanExportedEvent
    }


    @Override
    protected Class getServiceClass(T ref) {
        return super.getServiceClass(ref);
    }

}