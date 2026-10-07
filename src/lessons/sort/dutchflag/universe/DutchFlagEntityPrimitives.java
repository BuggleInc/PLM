package lessons.sort.dutchflag.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface DutchFlagEntityPrimitives extends EntityPrimitivesBase {
  @Primitive void swap(int fromLine, int toLine);

  @Primitive int getColor(int rank);

  @Primitive int getSize();

  @Primitive boolean isSorted();

  @Primitive boolean isSelected();

  @Primitive void assertSorted();
}
