package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class InterleavedHanoiEntity extends HanoiEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		solve(getParamInt(0), getParamInt(1), getParamInt(2), getParamInt(3));
	}

	def solve(src1:Int,src2:Int, other:Int, dst:Int): Unit = {
		interleavedHanoi(getSlotSize(src1), src1,src2, other,dst);
	}

	/* BEGIN TEMPLATE */
  def interleavedHanoi(height:Int, src1:Int, src2:Int, other:Int, dst:Int): Unit = {
	  /* BEGIN SOLUTION */
    if (height > 0) {
      hanoi(height-1, src1,dst,other);
      move(src1,dst);
      hanoi(height-1, src2,dst,src1);
      move(src2,dst);
      interleavedHanoi(height-1, other,src1,src2, dst);
    }    
  }
	def hanoi(height:Int, src:Int, other:Int, dst:Int): Unit = {
		if (height>0) {
			hanoi(height-1,  src,dst,other);
			move(src,dst);
			hanoi(height-1,  other,src,dst);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */

}
