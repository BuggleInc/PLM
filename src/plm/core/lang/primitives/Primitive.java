package plm.core.lang.primitives;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Marks a method of an entity primitives interface as callable from the student's process. The name defaults to the method's. */
@Retention(RetentionPolicy.RUNTIME) public @interface Primitive
{
  String name() default "";
}
