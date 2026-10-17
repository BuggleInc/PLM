package lessons.sort.pancake.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface PancakeEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(value = "Flips the given amount of pancakes, from the top of the stack.") void flip(int numberOfPancakes);

  @Primitive(value = "Returns the radius of the given pancake. The top-most pancake is of rank 0.") int getPancakeRadius(int pancakeNumber);

  @Primitive(value = "Returns the amount of pancakes in the stack.") int getStackSize();

  @Primitive(value = "Tells whether the given pancake is upside down, with its burned side on top. The top-most pancake is of rank 0.")
  boolean isPancakeUpsideDown(int rank);

  @Primitive(value = "Tells whether the stack is correctly sorted.") boolean isSorted();

  @Primitive(value = "Tells whether the current world is selected in the interface.") boolean isSelected();
}
