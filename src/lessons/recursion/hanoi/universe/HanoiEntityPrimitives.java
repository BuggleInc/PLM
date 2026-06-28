package lessons.recursion.hanoi.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface HanoiEntityPrimitives extends EntityPrimitivesBase {
    @Primitive(110)
    void move(int src, int dst);

    @Primitive(111)
    int getSlotSize(int slot);

    @Primitive(112)
    @Override
    boolean isSelected();
}
