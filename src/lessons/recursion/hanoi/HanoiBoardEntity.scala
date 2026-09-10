package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class HanoiBoardEntity extends HanoiEntity {

	override def run() {
		val src = getParamInt(0)
		hanoi(getSlotSize(src), src, getParamInt(1), getParamInt(2));
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
