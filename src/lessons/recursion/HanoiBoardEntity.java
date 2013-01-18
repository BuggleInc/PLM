package lessons.recursion;

import jlm.universe.hanoi.HanoiEntity;
import jlm.universe.hanoi.HanoiInvalidMove;

public class HanoiBoardEntity extends HanoiEntity {

	public void run() throws HanoiInvalidMove {
		solve((Integer)getParam(0),(Integer) getParam(1));
	}
	
	/* BEGIN TEMPLATE */
	public void solve(int src,int dst) throws HanoiInvalidMove {
		solve(src,dst, getSlotSize(src));
	}
	
	public void solve(int src, int dst, int height) throws HanoiInvalidMove {
		/* BEGIN SOLUTION */
		if (height!=0) {
			int other=-1;
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
