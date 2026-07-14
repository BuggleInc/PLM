package lessons.welcome.loopdowhile;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface PoucetEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(307) boolean crossing();

  @Primitive(306) boolean exitReached();
}
