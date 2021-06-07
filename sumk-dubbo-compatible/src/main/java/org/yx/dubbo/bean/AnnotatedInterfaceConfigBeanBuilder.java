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

import org.apache.dubbo.config.AbstractInterfaceConfig;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ModuleConfig;
import org.apache.dubbo.config.MonitorConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.yx.bean.IOC;
import org.yx.dubbo.ioc.BeanRegistry;
import org.yx.dubbo.spec.DubboBeanSpec;

import java.util.List;

/**
 * @author wjiajun
 * @param <C>
 */
public abstract class AnnotatedInterfaceConfigBeanBuilder<C extends AbstractInterfaceConfig> {

    protected final DubboBeanSpec dubboBeanSpec;

    protected Class<?> interfaceClass;

    protected AnnotatedInterfaceConfigBeanBuilder(DubboBeanSpec dubboBeanSpec) {
        this.dubboBeanSpec = dubboBeanSpec;
    }

    /**
     * Build {@link C}
     *
     * @return non-null
     */
    public final C build() throws Exception {

        checkDependencies();

        C configBean = doBuild();

        configureBean(configBean);

        return configBean;

    }

    private void checkDependencies() {

    }

    /**
     * Builds {@link C Bean}
     *
     * @return {@link C Bean}
     */
    protected abstract C doBuild();


    protected void configureBean(C configBean) throws Exception {

        preConfigureBean(dubboBeanSpec, configBean);

        configureParametersConfig(dubboBeanSpec, configBean);

        configureRegistryConfigs(configBean);

        configureMonitorConfig(configBean);

        configureApplicationConfig(configBean);

        configureModuleConfig(configBean);

        postConfigureBean(dubboBeanSpec, configBean);

    }

    /**
     * preConfigureBean
     *
     * @param dubboBeanSpec
     * @param configBean
     * @throws Exception
     */
    protected abstract void preConfigureBean(DubboBeanSpec dubboBeanSpec, C configBean) throws Exception;

    /**
     * configureParametersConfig
     *
     * @param dubboBeanSpec
     * @param referenceBean
     */
    protected abstract void configureParametersConfig(DubboBeanSpec dubboBeanSpec, C referenceBean);


    private void configureRegistryConfigs(C configBean) {

        String[] registryConfigBeanIds = resolveRegistryConfigBeanNames(dubboBeanSpec);

        List<RegistryConfig> registryConfigs = BeanRegistry.getBeans(registryConfigBeanIds, RegistryConfig.class);

        configBean.setRegistries(registryConfigs);

    }

    private void configureMonitorConfig(C configBean) {

        String monitorBeanName = resolveMonitorConfigBeanName(dubboBeanSpec);

        MonitorConfig monitorConfig = IOC.get(monitorBeanName, MonitorConfig.class);

        configBean.setMonitor(monitorConfig);

    }

    private void configureApplicationConfig(C configBean) {

        String applicationConfigBeanName = resolveApplicationConfigBeanName(dubboBeanSpec);

        ApplicationConfig applicationConfig = IOC.get(applicationConfigBeanName, ApplicationConfig.class);

        configBean.setApplication(applicationConfig);

    }

    private void configureModuleConfig(C configBean) {

        String moduleConfigBeanName = resolveModuleConfigBeanName(dubboBeanSpec);

        ModuleConfig moduleConfig = IOC.get(moduleConfigBeanName, ModuleConfig.class);

        configBean.setModule(moduleConfig);

    }

    /**
     * Resolves the configBean name of ModuleConfig
     *
     * @param dubboBeanSpec
     * @return
     */
    protected abstract String resolveModuleConfigBeanName(DubboBeanSpec dubboBeanSpec);

    /**
     * Resolves the configBean name of ApplicationConfig
     *
     * @param dubboBeanSpec
     * @return
     */
    protected abstract String resolveApplicationConfigBeanName(DubboBeanSpec dubboBeanSpec);


    /**
     * Resolves the configBean ids of RegistryConfig
     *
     * @param dubboBeanSpec
     * @return non-empty array
     */
    protected abstract String[] resolveRegistryConfigBeanNames(DubboBeanSpec dubboBeanSpec);

    /**
     * Resolves the configBean name of MonitorConfig
     *
     * @param dubboBeanSpec
     * @return
     */
    protected abstract String resolveMonitorConfigBeanName(DubboBeanSpec dubboBeanSpec);

    /**
     * Configures Bean
     *
     * @param dubboBeanSpec
     * @param configBean
     */
    protected abstract void postConfigureBean(DubboBeanSpec dubboBeanSpec, C configBean) throws Exception;

    public <T extends AnnotatedInterfaceConfigBeanBuilder<C>> T interfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        return (T) this;
    }
}