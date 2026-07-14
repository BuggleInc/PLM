package plm.test.simple;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface SimpleEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(500) void setObjectif(boolean b);
}
