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

  /**
   * Whether the hand-written glue of the universe (see CodeCreation.Remote.extraGlue()) wraps the generated functions, to offer the
   * students a more idiomatic API than the primitives themselves (e.g. the C system calls, with their buffers and structures). The
   * C and Python generators support it: the generated functions become private, named plm_remote_xxx in C (static) and
   * _plm_remote_xxx in Python (out of reach of "import *").
   */
  boolean wrapped() default false;
}
