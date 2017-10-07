package recursion.hanoi;


import plm.universe.hanoi.HanoiEntity

class ScalaSplitHanoi3Entity extends HanoiEntity {

	override def run() {
		solve(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Int], getParam(2).asInstanceOf[Int], getParam(3).asInstanceOf[Int]);
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
