package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class TricolorHanoi1Entity extends HanoiEntity {

	override def run() {
    val src = getParamInt(0)
    val mid = getParamInt(1)
    val dst = getParamInt(2)
		move3(getSlotSize(src)/3, src,mid, dst)
	}

	/* BEGIN TEMPLATE */
  def move3(height:Int, src:Int, mid:Int, dst:Int) {
	  /* BEGIN SOLUTION */
    if (height>0) {
//      System.err.println("move3("+height+","+src+","+dst+")");
      move3(height-1, src, dst, mid);
      move(src,dst);
      move(src,dst);
      move(src,dst);
      move3(height-1, mid, src, dst);
    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
