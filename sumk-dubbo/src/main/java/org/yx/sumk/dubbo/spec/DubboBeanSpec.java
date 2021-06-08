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

package org.yx.sumk.dubbo.spec;

import org.yx.sumk.dubbo.annotation.AnnotationAttributes;

/**
 * @author : wjiajun
 */
public class DubboBeanSpec {

    private Class<?> interfaceClass;

    private String interfaceName;

    private String version;

    private String group;

    private String application;

    private AnnotationAttributes annotationAttributes;

    public DubboBeanSpec(Class<?> interfaceClass, String interfaceName, String version, String group, String application, AnnotationAttributes annotationAttributes) {
        this.interfaceClass = interfaceClass;
        this.interfaceName = interfaceName;
        this.version = version;
        this.group = group;
        this.application = application;
        this.annotationAttributes = annotationAttributes;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public AnnotationAttributes getAnnotationAttributes() {
        return annotationAttributes;
    }

    public void setAnnotationAttributes(AnnotationAttributes annotationAttributes) {
        this.annotationAttributes = annotationAttributes;
    }
}