package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class LinearTwinHanoiEntity extends HanoiEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
    val src= getParamInt(0)
    val mid= getParamInt(1)
    val dst= getParamInt(2)
		linearTwinHanoi(getSlotSize(src), src,mid, dst);
	}

	/* BEGIN TEMPLATE */
  def linearTwinHanoi(height:Int, src:Int, mid:Int, dst:Int): Unit = {
	  /* BEGIN SOLUTION */
    gather(height-1,src,mid,dst);
    move(src,mid);
    moveDouble(height-1, dst, mid, src);
    move(dst,mid);
    moveDouble(height-1, src, mid, dst);
    move(mid, src);
    moveDouble(height-1, dst, mid, src);
    move(mid, dst);
    scatter(height-1, src, mid, dst);
  }
  def gather(height:Int, src:Int, mid:Int, dst:Int): Unit = {
    if (height >0) {
      gather(height-1,src,mid,dst);
      move(src,mid);
      moveDouble(height-1, dst,mid,src);
      move(mid,dst);
      moveDouble(height-1, src, mid, dst);
    }
  }
  def scatter(height:Int, src:Int, mid:Int, dst:Int): Unit = {
    if (height>0) {
      moveDouble(height-1, src, mid, dst);
      move(src,mid);
      moveDouble(height-1, dst, mid, src);
      move(mid,dst);
      scatter(height-1,src,mid,dst);
    }
  }
  def moveDouble(height:Int, src:Int, mid:Int, dst:Int): Unit = {
    if (height>0) {
      moveDouble(height-1, src, mid, dst);
      move(src,mid);
      move(src,mid);
      moveDouble(height-1, dst, mid, src);
      move(mid,dst);
      move(mid,dst);
      moveDouble(height-1, src, mid, dst);
    }

		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */

}
