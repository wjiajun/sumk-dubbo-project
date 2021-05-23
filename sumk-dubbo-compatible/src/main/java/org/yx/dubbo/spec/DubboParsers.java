package org.yx.dubbo.spec;

import org.yx.annotation.Bean;
import org.yx.annotation.Inject;
import org.yx.annotation.spec.BeanSpec;
import org.yx.annotation.spec.InjectSpec;

import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author : wjiajun
 */
public class DubboParsers {

    public static final Function<Class<?>, BeanSpec> BEAN_PARSER = clz -> {
        Bean c = clz.getAnnotation(Bean.class);
        if (c == null) {
            return null;
        }

        return new BeanSpec(c.value());
    };

    public static final BiFunction<Object, Field, InjectSpec> INJECT_PARSER = (src, f) -> {
        Inject c = f.getAnnotation(Inject.class);
        if (c == null) {
            return null;
        }

        return new InjectSpec(c.allowEmpty());
    };
}
