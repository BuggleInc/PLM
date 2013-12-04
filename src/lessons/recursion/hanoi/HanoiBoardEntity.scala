package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class ScalaHanoiBoardEntity extends HanoiEntity {

	override def run() {
		solve(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Int], getParam(2).asInstanceOf[Int]);
	}

	/* BEGIN TEMPLATE */
	def solve(src:Int, dst:Int, other:Int) {
		solve(src,dst,other, getSlotSize(src));
	}

	def solve(src:Int, dst:Int, other:Int, height:Int) {
		/* BEGIN SOLUTION */
		if (height!=0) {
			solve(src,other,dst, height-1);
			move(src,dst);
			solve(other,dst,src, height-1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
