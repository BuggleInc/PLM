package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.lang.primitives.EntityPrimitives;

@EntityPrimitives(lessons.recursion.hanoi.HanoiBoardEntity.class)
public class HanoiBoardEntity extends HanoiEntity {

  public void run() { solve(getParamInt(1), getParamInt(1), getParamInt(2)); }

  /* BEGIN TEMPLATE */
  public void solve(int src, int other, int dst)
  {
    /* BEGIN SOLUTION */
    hanoi(getSlotSize(src), src, other, dst);
  }

  public void hanoi(int height, int src, int other, int dst)
  {
    if (height != 0) {
      hanoi(height - 1, src, dst, other);
      move(src, dst);
      hanoi(height - 1, other, src, dst);
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
