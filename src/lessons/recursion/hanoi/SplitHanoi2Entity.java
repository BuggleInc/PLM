package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.lang.primitives.EntityPrimitives;

@EntityPrimitives(lessons.recursion.hanoi.SplitHanoi2Entity.class)
public class SplitHanoi2Entity extends HanoiEntity {

  public void run() { solve(getParamInt(0), getParamInt(1), getParamInt(2), getParamInt(3)); }

  /* BEGIN TEMPLATE */
  public void solve(int src, int other, int dst1, int dst2)
  {
    /* BEGIN SOLUTION */
    splitHanoi(getSlotSize(src) / 2, src, other, dst1, dst2);
  }

  public void splitHanoi(int height, int src, int other, int dst1, int dst2)
  {
    // for (int i=4;i>height;i--) System.out.print(" ");
    // System.out.println("solve("+height+","+src1+","+src2+","+other+","+dst+")");
    if (height > 0) {
      splitHanoi(height - 1, src, dst1, dst2, other);
      move(src, dst1);
      hanoi(height - 1, dst2, src, dst1);
      move(src, dst2);
      hanoi(height - 1, other, src, dst2);
    }
  }
  private void hanoi(int height, int src, int other, int dst)
  {
    // for (int i=4;i>height;i--) System.out.print(" ");
    // System.out.println("hanoi("+height+","+src+","+other+","+dst+")");
    if (height > 0) {
      hanoi(height - 1, src, dst, other);
      move(src, dst);
      hanoi(height - 1, other, src, dst);
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
