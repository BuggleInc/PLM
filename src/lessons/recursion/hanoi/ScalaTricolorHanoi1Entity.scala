package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.model.Game

class ScalaTricolorHanoi1Entity extends HanoiEntity {

	override def run():Unit = {
    val src = getParam(0).asInstanceOf[Int]
    val mid = getParam(1).asInstanceOf[Int]
    val dst = getParam(2).asInstanceOf[Int]
		move3(getSlotSize(src)/3, src,mid, dst)
	}

	/* BEGIN TEMPLATE */
  def move3(height:Int, src:Int, mid:Int, dst:Int):Unit = {
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
