package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class ScalaLinearHanoiEntity extends HanoiEntity {

	override def run() {
    val src= getParam(0).asInstanceOf[Int]
    val mid= getParam(1).asInstanceOf[Int]
    val dst= getParam(2).asInstanceOf[Int]
		linearHanoi(getSlotSize(src), src,mid, dst);
	}

	/* BEGIN TEMPLATE */
  def linearHanoi(height:Int, src:Int, mid:Int, dst:Int) {
	  /* BEGIN SOLUTION */
    if (height > 0) {
      linearHanoi(height-1, src,mid,dst);
      move(src,mid);
      linearHanoi(height-1, dst,mid,src);
      move(mid,dst);
      linearHanoi(height-1, src,mid,dst);
    }    
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
