package plm.core.lang.primitives;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import plm.universe.EntityPrimitivesBase;

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public @interface EntityPrimitives
{
  Class<? extends EntityPrimitivesBase> value();
}
