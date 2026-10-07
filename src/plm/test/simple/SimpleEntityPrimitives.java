package plm.test.simple;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface SimpleEntityPrimitives extends EntityPrimitivesBase {
  @Primitive void setObjectif(boolean b);
}
