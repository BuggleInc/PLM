package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

public class HanoiBoardEntity extends HanoiEntity {

	public void run() {
		solve((Integer)getParam(0),(Integer) getParam(1),(Integer) getParam(2));
	}

	/* BEGIN TEMPLATE */
	public void solve(int src, int dst, int other) {
		solve(src, dst, other, getSlotSize(src));
	}

	public void solve(int src, int dst, int other, int height) {
		/* BEGIN SOLUTION */
		if (height != 0) {
			solve(src,other,dst, height-1);
			move(src,dst);
			solve(other,dst,src, height-1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
