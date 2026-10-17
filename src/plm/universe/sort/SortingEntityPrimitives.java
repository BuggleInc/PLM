package plm.universe.sort;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface SortingEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(value = "Copies the value of cell fromPos into cell toPos. Costs one read and one write.") void copy(int fromPos, int toPos);

  @Primitive(value = "Returns the value of cell i. Costs one read.") int getValue(int i);

  @Primitive(value = "Returns the amount of values in the array. Costs nothing.") int getValueCount();

  @Primitive(value = "Tells whether the value of cell i is strictly smaller than the one of cell j. Costs two reads.") boolean isSmaller(int i, int j);

  @Primitive(value = "Tells whether the value of cell i is strictly smaller than the given value. Costs one read.") boolean isSmallerThan(int i, int value);

  @Primitive(value = "Sets the value of cell i. Costs one write.") void setValue(int i, int value);

  @Primitive(value = "Swaps the values of cells i and j. Costs two reads and two writes.") void swap(int i, int j);

  @Primitive(value = "Tells whether the current world is selected in the interface. Costs nothing.") @Override boolean isSelected();
}