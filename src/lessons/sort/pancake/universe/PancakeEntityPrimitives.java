package lessons.sort.pancake.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface PancakeEntityPrimitives extends EntityPrimitivesBase {
  @Primitive void flip(int numberOfPancakes);

  @Primitive int getPancakeRadius(int pancakeNumber);

  @Primitive int getStackSize();

  @Primitive boolean isPancakeUpsideDown(int rank);

  @Primitive boolean isSorted();

  @Primitive boolean isSelected();
}
