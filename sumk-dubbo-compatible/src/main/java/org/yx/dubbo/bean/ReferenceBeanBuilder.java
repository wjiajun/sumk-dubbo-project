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

import com.google.common.collect.Iterables;
import org.apache.dubbo.common.utils.Assert;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.annotation.Method;
import org.yx.bean.IOC;
import org.yx.dubbo.annotation.AnnotationAttributes;
import org.yx.dubbo.annotation.AnnotationResolver;
import org.yx.dubbo.spec.DubboBeanSpec;
import org.yx.dubbo.utils.ResolveUtils;
import org.yx.util.kit.Asserts;

import java.util.List;

/**
 * @author wjiajun
 */
class ReferenceBeanBuilder extends AnnotatedInterfaceConfigBeanBuilder<ReferenceBean> {

    private ReferenceBeanBuilder(DubboBeanSpec dubboBeanSpec) {
        super(dubboBeanSpec);
    }

    private void configureInterface(DubboBeanSpec dubboBeanSpec, ReferenceBean referenceBean) {
        boolean generic = dubboBeanSpec.getAnnotationAttributes().getBoolean("generic");
        if (generic) {
            // it's a generic reference
            String interfaceClassName = dubboBeanSpec.getAnnotationAttributes().getString("interfaceName");
            Assert.notEmptyString(interfaceClassName, "@Reference interfaceName() must be present when reference a generic service!");
            referenceBean.setInterface(interfaceClassName);
            return;
        }

        Class<?> serviceInterfaceClass = AnnotationResolver.resolveServiceInterfaceClass(dubboBeanSpec.getAnnotationAttributes(), interfaceClass);

        Asserts.requireTrue(serviceInterfaceClass.isInterface(), "The class of field or method that was annotated @Reference is not an interface!");

        referenceBean.setInterface(serviceInterfaceClass);

    }


    private void configureConsumerConfig(DubboBeanSpec dubboBeanSpec, ReferenceBean<?> referenceBean) {

        String consumerBeanName = dubboBeanSpec.getAnnotationAttributes().getString("consumer");

        ConsumerConfig consumerConfig = Iterables.getFirst(IOC.getBeans(consumerBeanName, ConsumerConfig.class), null);
        referenceBean.setConsumer(consumerConfig);
    }

    @Override
    protected void configureParametersConfig(DubboBeanSpec dubboBeanSpec, ReferenceBean referenceBean) {
        AnnotationAttributes annotationAttributes = dubboBeanSpec.getAnnotationAttributes();
        referenceBean.setParameters(ResolveUtils.convertParameters(annotationAttributes.getStringArray("parameters")));
    }

    void configureMethodConfig(DubboBeanSpec dubboBeanSpec, ReferenceBean<?> referenceBean) {
        Method[] methods = dubboBeanSpec.getAnnotationAttributes().getAnnotationArray("methods", Method.class);
        List<MethodConfig> methodConfigs = MethodConfig.constructMethodConfig(methods);
        if (!methodConfigs.isEmpty()) {
            referenceBean.setMethods(methodConfigs);
        }
    }

    @Override
    protected ReferenceBean doBuild() {
        return ReferenceBeanFactory.create(dubboBeanSpec.getAnnotationAttributes().annotation());
    }

    @Override
    protected void preConfigureBean(DubboBeanSpec dubboBeanSpec, ReferenceBean referenceBean) {
        Assert.notNull(interfaceClass, "The interface class must set first!");

        // Bind annotation attributes
        // 改为构造器绑定
    }

    @Override
    protected String resolveModuleConfigBeanName(DubboBeanSpec dubboBeanSpec) {
        return dubboBeanSpec.getAnnotationAttributes().getString("module");
    }

    @Override
    protected String resolveApplicationConfigBeanName(DubboBeanSpec dubboBeanSpec) {
        return dubboBeanSpec.getAnnotationAttributes().getString("application");
    }

    @Override
    protected String[] resolveRegistryConfigBeanNames(DubboBeanSpec dubboBeanSpec) {
        return dubboBeanSpec.getAnnotationAttributes().getStringArray("registry");
    }

    @Override
    protected String resolveMonitorConfigBeanName(DubboBeanSpec dubboBeanSpec) {
        return dubboBeanSpec.getAnnotationAttributes().getString("monitor");
    }

    @Override
    protected void postConfigureBean(DubboBeanSpec dubboBeanSpec, ReferenceBean bean) throws Exception {

        configureInterface(dubboBeanSpec, bean);

        configureConsumerConfig(dubboBeanSpec, bean);

        configureMethodConfig(dubboBeanSpec, bean);

        bean.afterPropertiesSet();

    }

    public static ReferenceBeanBuilder create(DubboBeanSpec dubboBeanSpec) {
        return new ReferenceBeanBuilder(dubboBeanSpec);
    }
}