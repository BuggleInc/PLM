package lessons.recursion.hanoi;

import plm.universe.hanoi.HanoiEntity;

public class TricolorHanoi1Entity extends HanoiEntity {
	
	public void run() {
		int src = (Integer)getParam(0);
		int mid = (Integer)getParam(1);
		int dst = (Integer)getParam(2);
		move3(getSlotSize(src)/3, src, mid, dst);
	}

	/* BEGIN TEMPLATE */
	void move3(int height, int src, int mid, int dst) {
		/* BEGIN SOLUTION */
		if (height>0) {
//			System.err.println("move3("+height+","+src+","+dst+")");
			move3(height-1, src, dst, mid);
			move(src,dst);
			move(src,dst);
			move(src,dst);
			move3(height-1, mid, src, dst);
		}
		/* END SOLUTION */
	}

	/* END TEMPLATE */

}
