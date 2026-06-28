package lessons.sort.baseball.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface BaseballEntityPrimitives extends EntityPrimitivesBase {
    @Primitive(110)
    int getBasesAmount();

    @Primitive(111)
    int getPositionsAmount();

    @Primitive(114)
    int getPlayerColor(int base, int position);

    @Primitive(116)
    boolean isBaseSorted(int base);

    @Primitive(115)
    boolean isSorted();

    @Primitive(112)
    int getHoleBase();

    @Primitive(113)
    int getHolePosition();

    @Primitive(118)
    void move(int base, int position);

    @Primitive(117)
    @Override
    boolean isSelected();

    @Primitive(119)
    void assertSorted(String str);
}
