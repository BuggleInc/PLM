package plm.universe.bat;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface BatEntityPrimitives extends EntityPrimitivesBase {
  @Primitive int getTestCount();
  @Primitive String getTest(int i);
  @Primitive void setTestResult(int i, String str);
}
