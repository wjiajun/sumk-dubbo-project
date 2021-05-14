package org.yx.dubbo.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author : wjiajun
 * @description:
 */
public class ReflectionUtils {

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}
