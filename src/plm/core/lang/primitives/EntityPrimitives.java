package plm.core.lang.primitives;

import plm.universe.EntityPrimitivesBase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityPrimitives {
    Class<? extends EntityPrimitivesBase> value();
}
