package plm.core.lang.primitives;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a method of an entity primitives interface as callable from the student's process, under the method's name. The English
 * texts are extracted by {@link PrimitivePot} and translated when displayed.
 */
@Retention(RetentionPolicy.RUNTIME) public @interface Primitive
{
  /** Documentation shown in the "About this world" dialog. Undocumented primitives are neither shown nor translated. */
  String value() default "";

  /** Title of the group of primitives under which it is documented. */
  String group() default "";
}
