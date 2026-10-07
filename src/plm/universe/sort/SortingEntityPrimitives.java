package plm.universe.sort;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface SortingEntityPrimitives extends EntityPrimitivesBase {
  @Primitive void copy(int fromPos, int toPos);

  @Primitive int getValue(int i);

  @Primitive int getValueCount();

  @Primitive boolean isSmaller(int i, int j);

  @Primitive boolean isSmallerThan(int i, int value);

  @Primitive void setValue(int i, int value);

  @Primitive void swap(int i, int j);

  @Primitive @Override boolean isSelected();
}