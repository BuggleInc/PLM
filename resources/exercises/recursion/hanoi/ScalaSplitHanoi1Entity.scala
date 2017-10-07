package recursion.hanoi;


import plm.universe.hanoi.HanoiEntity

class ScalaSplitHanoi1Entity extends HanoiEntity {

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
      moveDouble(height-1, src,dst1,dst2,other);
      move(src,dst1);
      move(src,dst2);
      splitHanoi(height-1, other,src,dst1, dst2);
    }    
  }
  def moveDouble(height:Int, src:Int, other1:Int, other2:Int, dst:Int) {
    //for (int i=4;i>height;i--) System.out.print(" ");
    //Logger.debug("hanoi("+height+","+src+","+other+","+dst+")");
    if (height>0) {
      moveDouble(height-1, src,other1,dst,other2);
      move(src,other1);
      move(src,dst);
      move(other1,dst);
      moveDouble(height-1, other2, src, other1,dst);
    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
