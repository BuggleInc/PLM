package plm.core.lang.primitives;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Order in which the groups of primitives declared by this primitives interface (or entity) are documented, by English title. */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public @interface PrimitiveGroups
{
  String[] value();
}
