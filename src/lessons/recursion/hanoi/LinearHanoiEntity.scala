package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class LinearHanoiEntity extends HanoiEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
    val src= getParamInt(0)
    val mid= getParamInt(1)
    val dst= getParamInt(2)
		linearHanoi(getSlotSize(src), src,mid, dst);
	}

	/* BEGIN TEMPLATE */
  def linearHanoi(height:Int, src:Int, mid:Int, dst:Int): Unit = {
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
	/* END REMOTE */

}
