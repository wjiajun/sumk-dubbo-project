package org.yx.dubbo.annotation;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.apache.dubbo.common.utils.AnnotationUtils.getAttribute;
import static org.apache.dubbo.common.utils.ArrayUtils.isNotEmpty;
import static org.apache.dubbo.common.utils.ClassUtils.isGenericClass;
import static org.apache.dubbo.common.utils.ClassUtils.resolveClass;
import static org.apache.dubbo.common.utils.StringUtils.isEmpty;

/**
 * The resolver class for {@link Service @Service}
 *
 * @see DubboReference
 * @see com.alibaba.dubbo.config.annotation.Reference
 * @see Reference
 * @since 2.7.6
 */
public class ReferenceAnnotationResolver {

    public static List<Class<? extends Annotation>> REFERENCE_ANNOTATION_CLASSES =
            Collections.unmodifiableList(Arrays.asList(DubboReference.class, Reference.class, com.alibaba.dubbo.config.annotation.Reference.class));

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
        Iterator referenceIterator = REFERENCE_ANNOTATION_CLASSES.iterator();

        while(referenceIterator.hasNext()) {
            Class<? extends Annotation> referenceAnnotationClass = (Class)referenceIterator.next();
            referenceAnnotation = referenceField.getAnnotation(referenceAnnotationClass);
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

        Class interfaceClass = null;
        // first, try to get the value from "interfaceName" attribute
        String interfaceName = resolveAttribute("interfaceName");

        if (isEmpty(interfaceName)) { // If not found, try "interfaceClass"
            interfaceClass = resolveAttribute("interfaceClass");
        } else {
            interfaceClass = resolveClass(interfaceName, getClass().getClassLoader());
        }

        if (isGenericClass(interfaceClass)) {
            interfaceName = interfaceClass.getName();
        } else {
            interfaceName = null;
        }

        if (isEmpty(interfaceName)) { // If not fund, try to get the first interface from the service type
            Class[] interfaces = referenceType.getInterfaces();
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

    public Class<?> getReferenceType() {
        return referenceType;
    }
}
