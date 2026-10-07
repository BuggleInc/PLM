package lessons.sort.baseball.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface BaseballEntityPrimitives extends EntityPrimitivesBase {
  @Primitive int getBasesAmount();

  @Primitive int getPositionsAmount();

  @Primitive int getPlayerColor(int base, int position);

  @Primitive boolean isBaseSorted(int base);

  @Primitive boolean isSorted();

  @Primitive int getHoleBase();

  @Primitive int getHolePosition();

  @Primitive void move(int base, int position);

  @Primitive @Override boolean isSelected();

  @Primitive void assertSorted(String str);
}
