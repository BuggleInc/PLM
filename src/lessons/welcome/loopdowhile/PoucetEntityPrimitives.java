package lessons.welcome.loopdowhile;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface PoucetEntityPrimitives extends EntityPrimitivesBase {
  @Primitive boolean crossing();

  @Primitive boolean exitReached();
}
