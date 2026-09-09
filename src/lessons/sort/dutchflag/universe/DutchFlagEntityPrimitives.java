package lessons.sort.dutchflag.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface DutchFlagEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(110) void swap(int fromLine, int toLine);

  @Primitive(112) int getColor(int rank);

  @Primitive(111) int getSize();

  @Primitive(113) boolean isSorted();

  @Primitive(114) boolean isSelected();

  @Primitive(115) void assertSorted();
}
