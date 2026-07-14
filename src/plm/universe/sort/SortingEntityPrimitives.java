package plm.universe.sort;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface SortingEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(114) void copy(int from, int to);

  @Primitive(115) int getValue(int i);

  @Primitive(110) int getValueCount();

  @Primitive(111) boolean isSmaller(int i, int j);

  @Primitive(112) boolean isSmallerThan(int i, int val);

  @Primitive(116) void setValue(int i, int val);

  @Primitive(113) void swap(int i, int j);

  @Primitive(117) @Override boolean isSelected();
}