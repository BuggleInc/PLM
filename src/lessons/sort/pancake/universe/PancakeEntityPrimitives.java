package lessons.sort.pancake.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface PancakeEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(113) void flip(int numberOfPancakes);

  @Primitive(111) int getPancakeRadius(int pancakeNumber);

  @Primitive(110) int getStackSize();

  @Primitive(112) boolean isPancakeUpsideDown(int rank);

  @Primitive(114) boolean isSorted();

  @Primitive(115) boolean isSelected();
}
