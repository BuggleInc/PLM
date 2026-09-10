package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class TricolorHanoi2Entity extends HanoiEntity {

	override def run() {
    val src = getParamInt(0)
    val mid = getParamInt(1)
    val dst = getParamInt(2)
    gather(getSlotSize(src), src, mid, dst)
	}

	/* BEGIN TEMPLATE */
	def gather(height:Int, src:Int, mid:Int, dst:Int) {
		/* BEGIN SOLUTION */
    if (height >0) {
      gather(height-1,src,mid,dst);
      move(src,mid);
      move3(height-1, dst,mid,src);
      move(mid,dst);
      move(mid,dst);
      move3(height-1, src, mid, dst);
    }
	}

  def move3(height:Int, src:Int, mid:Int, dst:Int) {
    if (height>0) {
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
