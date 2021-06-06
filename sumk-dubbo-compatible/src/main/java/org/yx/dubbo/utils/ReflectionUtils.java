package org.yx.dubbo.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author : wjiajun
 */
public class ReflectionUtils {

    public static void makeAccessible(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            method.setAccessible(true);
        }

        if ((!Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}
