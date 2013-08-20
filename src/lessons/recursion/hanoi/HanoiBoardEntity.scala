package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class ScalaHanoiBoardEntity extends HanoiEntity {

	override def run() {
		solve(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Int]);
	}

	/* BEGIN TEMPLATE */
	def solve(src:Int, dst:Int) {
		solve(src,dst, getSlotSize(src));
	}

	def solve(src:Int, dst:Int, height:Int) {
		/* BEGIN SOLUTION */
		if (height!=0) {
			var other = -1;
			if (src+dst==1) /* 0+1 */
				other=2;
			if (src+dst==2) /* 0+2 */
				other=1;
			if (src+dst==3) /* 1+2 */
				other=0;

			solve(src,other, height-1);
			move(src,dst);
			solve(other,dst,height-1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
