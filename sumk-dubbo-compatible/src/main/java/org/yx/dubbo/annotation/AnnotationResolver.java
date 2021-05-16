package org.yx.dubbo.annotation;


import org.apache.dubbo.common.utils.Assert;
import org.apache.dubbo.common.utils.ClassUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.yx.conf.AppInfo;
import org.yx.dubbo.utils.ObjectUtils;
import org.yx.dubbo.utils.ReflectionUtils;
import org.yx.util.kit.Asserts;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wjiajun
 * @description: 注解解析
 */
public class AnnotationResolver {

    private static final Map<Class<? extends Annotation>, List<Method>> attributeMethodsCache =
            new ConcurrentHashMap<Class<? extends Annotation>, List<Method>>(256);

    public static AnnotationAttributes getAnnotationAttributes(Annotation annotation) {
        AnnotationAttributes attributes =
                retrieveAnnotationAttributes(annotation);
        postProcessAnnotationAttributes(attributes);
        return attributes;
    }

    /**
     * Resolve the {@link Class class} of Dubbo Service interface from the specified
     * {@link AnnotationAttributes annotation attributes} and annotated {@link Class class}.
     *
     * @param attributes            {@link AnnotationAttributes annotation attributes}
     * @param defaultInterfaceClass the annotated {@link Class class}.
     * @return the {@link Class class} of Dubbo Service interface
     * @throws IllegalArgumentException if can't resolved
     */
    public static Class<?> resolveServiceInterfaceClass(AnnotationAttributes attributes, Class<?> defaultInterfaceClass)
            throws IllegalArgumentException {

        ClassLoader classLoader = defaultInterfaceClass != null ? defaultInterfaceClass.getClassLoader() : Thread.currentThread().getContextClassLoader();

        Class<?> interfaceClass = attributes.getClass("interfaceClass");

        if (void.class.equals(interfaceClass)) { // default or set void.class for purpose.

            interfaceClass = null;

            String interfaceClassName = attributes.getString("interfaceName");

            if (StringUtils.isNoneEmpty(interfaceClassName)) {
                if (ClassUtils.isPresent(interfaceClassName, classLoader)) {
                    interfaceClass = ClassUtils.resolveClass(interfaceClassName, classLoader);
                }
            }

        }

        if (interfaceClass == null && defaultInterfaceClass != null) {
            // Find all interfaces from the annotated class
            Set<Class<?>> allInterfaces = org.yx.dubbo.utils.ClassUtils.getAllInterfacesForClassAsSet(defaultInterfaceClass, null);

            if (!allInterfaces.isEmpty()) {
                interfaceClass = allInterfaces.toArray(new Class<?>[0])[0];
            }

        }

        Assert.notNull(interfaceClass,
                "@Service interfaceClass() or interfaceName() or interface class must be present!");

        Asserts.requireTrue(interfaceClass.isInterface(),
                "The annotated type must be an interface!");

        return interfaceClass;
    }

    static AnnotationAttributes retrieveAnnotationAttributes(Annotation annotation) {

        Class<? extends Annotation> annotationType = annotation.annotationType();
        AnnotationAttributes attributes = new AnnotationAttributes(annotation);

        for (Method method : getAttributeMethods(annotationType)) {
            try {
                Object attributeValue = method.invoke(annotation);
                Object defaultValue = method.getDefaultValue();
                if (defaultValue != null && ObjectUtils.nullSafeEquals(attributeValue, defaultValue)) {
                    attributeValue = new DefaultValueHolder(defaultValue);
                }
                attributes.put(method.getName(), adaptValue(attributeValue));
            } catch (Throwable ex) {
                throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
            }
        }

        return attributes;
    }

    static void postProcessAnnotationAttributes(AnnotationAttributes attributes) {

        // Abort?
        if (attributes == null) {
            return;
        }

        // Replace any remaining placeholders with actual default values
        for (String attributeName : attributes.keySet()) {
            Object value = attributes.get(attributeName);
            if (value instanceof DefaultValueHolder) {
                value = ((DefaultValueHolder) value).defaultValue;
                attributes.put(attributeName, adaptValue(value));
            }
        }
    }

    static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        List<Method> methods = attributeMethodsCache.get(annotationType);
        if (methods != null) {
            return methods;
        }

        methods = new ArrayList<Method>();
        for (Method method : annotationType.getDeclaredMethods()) {
            if (isAttributeMethod(method)) {
                ReflectionUtils.makeAccessible(method);
                methods.add(method);
            }
        }

        attributeMethodsCache.put(annotationType, methods);
        return methods;
    }

    static boolean isAttributeMethod(Method method) {
        return (method != null && method.getParameterTypes().length == 0 && method.getReturnType() != void.class);
    }

    static Object adaptValue(Object value) {

        if (value instanceof Annotation) {
            Annotation annotation = (Annotation) value;
            return getAnnotationAttributes(annotation);

        }

        if (value instanceof Annotation[]) {
            Annotation[] annotations = (Annotation[]) value;
            AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[annotations.length];
            for (int i = 0; i < annotations.length; i++) {
                mappedAnnotations[i] =
                        getAnnotationAttributes(annotations[i]);
                return mappedAnnotations;
            }

        }

        if (value instanceof String && ((String) value).contains("${")) {
            String replace = ((String) value).replace("${", "").replace("}", "");
            return AppInfo.getLatin(replace);
        }

        // Fallback
        return value;
    }

    private static class DefaultValueHolder {

        final Object defaultValue;

        public DefaultValueHolder(Object defaultValue) {
            this.defaultValue = defaultValue;
        }
    }
}
