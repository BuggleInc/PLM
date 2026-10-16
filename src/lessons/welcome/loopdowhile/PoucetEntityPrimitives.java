package lessons.welcome.loopdowhile;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface PoucetEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(value = "Tells whether the buggle is on a crossing.", group = "Specific to this exercise") boolean crossing();

  @Primitive(value = "Tells whether the buggle reached the exit.", group = "Specific to this exercise") boolean exitReached();
}
