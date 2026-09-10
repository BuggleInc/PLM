package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class SplitHanoi2Entity extends HanoiEntity {

	override def run() {
		solve(getParamInt(0), getParamInt(1), getParamInt(2), getParamInt(3));
	}

	def solve(src:Int,other:Int, dst1:Int, dst2:Int) {
		splitHanoi(getSlotSize(src)/2, src,other, dst1,dst2);
	}

	/* BEGIN TEMPLATE */
  def splitHanoi(height:Int, src:Int, other:Int, dst1:Int, dst2:Int) {
	  /* BEGIN SOLUTION */
    if (height > 0) {
      splitHanoi(height-1, src,dst1,dst2,other);
      move(src,dst1);
      hanoi(height-1, dst2,src,dst1);
      move(src,dst2);
      hanoi(height-1, other,src,dst2);
    }    
  }
  def hanoi(height:Int, src:Int, other:Int, dst:Int) {
    if (height>0) {
      hanoi(height-1,  src,dst,other);
      move(src,dst);
      hanoi(height-1,  other,src,dst);
    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
