package lessons.recursion.hanoi;

import plm.universe.hanoi.HanoiEntity;

public class TricolorHanoi3Entity extends HanoiEntity {
	
	public void run() {
		int src = (Integer)getParam(0);
		int mid = (Integer)getParam(1);
		int dst = (Integer)getParam(2);
		tricolor(getSlotSize(src),src,mid,dst);
	}

	/* BEGIN TEMPLATE */
	void tricolor(int height, int src, int mid, int dst) {
		/* BEGIN SOLUTION */
		gather(height, src, mid, dst);
		move3(height, dst,mid,src);
		move3(height, src,dst,mid);
		scatter(height, mid,dst,src);
	}
	void scatter(int height, int src, int mid, int dst) {
		if (height >0) {
			move3(height-1, src, dst,mid);
			move(src,dst);
			move(src,dst);
			move3(height-1, mid,dst,src);
			move(dst,mid);
			scatter(height-1, src,mid,dst);
		}		
	}
	void gather(int height, int src, int mid, int dst) {
		if (height >0) {
			//System.err.println("Gather("+height+","+src+","+mid+","+dst+")");
			gather(height-1,src,mid,dst);
			move(src,mid);
			move3(height-1, dst,mid,src);
			move(mid,dst);
			move(mid,dst);
			move3(height-1, src, mid, dst);
			//System.err.println("End gather("+height+")");
		}
	}
	void move3(int height, int src, int mid, int dst) {
		if (height>0) {
			//System.err.println("move3("+height+","+src+","+dst+")");
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
