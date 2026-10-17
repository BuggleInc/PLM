package lessons.recursion.hanoi.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface HanoiEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(value = "Moves the top-most disk from slot src onto slot dst. Fails on invalid moves, such as putting a disk over a smaller one.")
  void move(int src, int dst);

  @Primitive(value = "Returns the amount of disks on the given slot.") int getSlotSize(int slot);

  @Primitive(value = "Tells whether the current world is selected in the interface.") @Override boolean isSelected();

  @Primitive public void cyclicMove(int src, int dst);
}
