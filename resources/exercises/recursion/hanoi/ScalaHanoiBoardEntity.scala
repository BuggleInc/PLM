package recursion.hanoi;


import plm.universe.hanoi.HanoiEntity

class ScalaHanoiBoardEntity extends HanoiEntity {

	override def run() {
		solve(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Int], getParam(2).asInstanceOf[Int]);
	}

	def solve(src:Int, other:Int, dst:Int) {
		hanoi(getSlotSize(src), src,other,dst);
	}

	/* BEGIN TEMPLATE */
	def hanoi(height:Int, src:Int, other:Int, dst:Int) {
		/* BEGIN SOLUTION */
		if (height!=0) {
			hanoi(height-1,  src,dst,other);
			move(src,dst);
			hanoi(height-1,  other,src,dst);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
