package lessons.recursion.hanoi.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface HanoiEntityPrimitives extends EntityPrimitivesBase {
  @Primitive void move(int src, int dst);

  @Primitive int getSlotSize(int slot);

  @Primitive @Override boolean isSelected();

  @Primitive public void cyclicMove(int src, int dst);
}
