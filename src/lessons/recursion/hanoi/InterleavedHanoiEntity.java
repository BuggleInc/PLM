package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

public class InterleavedHanoiEntity extends HanoiEntity {

  public void run() { solve(getParamInt(0), getParamInt(1), getParamInt(2), getParamInt(3)); }
  /* BEGIN TEMPLATE */
  public void solve(int src1, int src2, int other, int dst)
  {
    /* BEGIN SOLUTION */
    interleavedHanoi(getSlotSize(src1), src1, src2, other, dst);
  }

  public void interleavedHanoi(int height, int src1, int src2, int other, int dst)
  {
    // for (int i=4;i>height;i--) System.out.print(" ");
    // System.out.println("solve("+height+","+src1+","+src2+","+other+","+dst+")");
    if (height > 0) {
      hanoi(height - 1, src1, dst, other);
      move(src1, dst);
      hanoi(height - 1, src2, dst, src1);
      move(src2, dst);
      interleavedHanoi(height - 1, other, src1, src2, dst);
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
