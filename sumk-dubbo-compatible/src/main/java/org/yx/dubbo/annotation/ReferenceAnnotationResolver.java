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

package org.yx.dubbo.annotation;

import org.apache.dubbo.config.annotation.DubboReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.apache.dubbo.common.utils.AnnotationUtils.getAttribute;
import static org.apache.dubbo.common.utils.ArrayUtils.isNotEmpty;
import static org.apache.dubbo.common.utils.ClassUtils.isGenericClass;
import static org.apache.dubbo.common.utils.ClassUtils.resolveClass;
import static org.apache.dubbo.common.utils.StringUtils.isEmpty;

/**
 * The resolver class for {@link DubboReference @Service}
 *
 * @author wjiajun
 * @see DubboReference
 * @since 2.7.6
 */
public class ReferenceAnnotationResolver {

    public static List<Class<? extends Annotation>> REFERENCE_ANNOTATION_CLASSES = Collections.singletonList(DubboReference.class);

    private final Annotation referenceAnnotation;

    private final Class<?> referenceType;

    private final Field referenceField;

    public ReferenceAnnotationResolver(Class<?> referenceType, Field f) throws IllegalArgumentException {
        this.referenceType = referenceType;
        this.referenceField = f;
        this.referenceAnnotation = getReferenceAnnotation(referenceType);
    }

    private Annotation getReferenceAnnotation(Class<?> referenceType) {
        Annotation referenceAnnotation = null;

        for (Class<? extends Annotation> annotationClass : REFERENCE_ANNOTATION_CLASSES) {
            referenceAnnotation = referenceField.getAnnotation(annotationClass);
            if (referenceAnnotation != null) {
                break;
            }
        }

        if (referenceAnnotation == null) {
            throw new IllegalArgumentException(String.format("Any annotation of [%s] can't be annotated in the reference type[%s].", REFERENCE_ANNOTATION_CLASSES, referenceType.getName()));
        } else {
            return referenceAnnotation;
        }
    }

    /**
     * Resolve the class name of interface
     *
     * @return if not found, return <code>null</code>
     */
    public String resolveInterfaceClassName() {

        Class<?> interfaceClass;
        String interfaceName = resolveAttribute("interfaceName");

        if (isEmpty(interfaceName)) {
            interfaceClass = resolveAttribute("interfaceClass");
        } else {
            interfaceClass = resolveClass(interfaceName, getClass().getClassLoader());
        }

        if (isGenericClass(interfaceClass)) {
            interfaceName = interfaceClass.getName();
        } else {
            interfaceName = null;
        }

        if (isEmpty(interfaceName)) {
            Class<?>[] interfaces = referenceType.getInterfaces();
            if (isNotEmpty(interfaces)) {
                interfaceName = interfaces[0].getName();
            }
        }

        return interfaceName;
    }

    public String resolveVersion() {
        return resolveAttribute("version");
    }

    public String resolveGroup() {
        return resolveAttribute("group");
    }

    private <T> T resolveAttribute(String attributeName) {
        return getAttribute(referenceAnnotation, attributeName);
    }

    public Annotation getReferenceAnnotation() {
        return referenceAnnotation;
    }
}