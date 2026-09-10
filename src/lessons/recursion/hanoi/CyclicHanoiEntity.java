package lessons.recursion.hanoi;

public class CyclicHanoiEntity extends lessons.recursion.hanoi.universe.HanoiEntity {

  public void move(int src, int dst)
  {
    // System.err.println("move("+from+","+to+")");
    cyclicMove(src, dst);
  }

  public void run() { clockwise(getSlotSize(getParamInt(0)), getParamInt(0), getParamInt(1), getParamInt(2)); }

  /* BEGIN TEMPLATE */
  void clockwise(int height, int src, int mid, int dst)
  {
    /* BEGIN SOLUTION */
    if (height > 0) {
      // System.err.println("beg clockwise("+height+","+src+","+mid+","+dst+")");
      anti(height - 1, src, dst, mid);
      move(src, dst);
      anti(height - 1, mid, src, dst);
      // System.err.println("end clockwise("+height+","+src+","+mid+","+dst+")");
    }
  }
  void anti(int height, int src, int mid, int dst)
  {
    if (height > 0) {
      // System.err.println("beg counterclockwise("+height+","+src+","+mid+","+dst+")");
      anti(height - 1, src, mid, dst);
      move(src, mid);
      clockwise(height - 1, dst, mid, src);
      move(mid, dst);
      anti(height - 1, src, mid, dst);
      // System.err.println("end counterclockwise("+height+","+src+","+mid+","+dst+")");
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
