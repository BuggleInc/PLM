package plm.universe.bat;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface BatEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(100) int getTestCount();
  @Primitive(101) String getTest(int i);
  @Primitive(102) void setTestResult(int i, String str);
}
