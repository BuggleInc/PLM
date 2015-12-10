package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.model.Game

class ScalaHanoiBoardEntity extends HanoiEntity {

	override def run():Unit = {
		solve(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Int], getParam(2).asInstanceOf[Int]);
	}

	def solve(src:Int, other:Int, dst:Int):Unit = {
		hanoi(getSlotSize(src), src,other,dst);
	}

	/* BEGIN TEMPLATE */
	def hanoi(height:Int, src:Int, other:Int, dst:Int):Unit = {
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
