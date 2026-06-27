package plm.core.lang.primitives;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Primitive {
    int value();
    String name() default "";
}
