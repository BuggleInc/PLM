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
   * students a more idiomatic API than the primitives themselves (e.g. the C system calls, with their buffers and structures). Every
   * generator supports it: the generated functions become private, named plm_remote_xxx in C (static), Java and Scala, and
   * _plm_remote_xxx in Python (out of reach of "import *").
   */
  boolean wrapped() default false;

  /**
   * The classes of the JDK that the hand-written Java and Scala glues replace with nested classes of the same simple name, such as
   * "java.net.Socket": the generated Java and Scala entities import the glue's ones by name, which beats the imports on demand of the
   * entity (such as java.net.* or java.net._, and the implicit java.lang ones), while its own imports of the replaced classes are
   * dropped.
   */
  String[] replaces() default {};
}
