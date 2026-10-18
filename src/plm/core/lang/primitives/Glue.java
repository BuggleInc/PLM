package plm.core.lang.primitives;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the root entity class of a universe: CodeCreation generates, in every language, a glue of that name exposing the primitives
 * of the entity and of all its subclasses. The student's entities import it (e.g. "from RemoteBuggle import *").
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public @interface Glue
{
  String value();
}
