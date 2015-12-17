package lessons.recursion.hanoi;

import plm.universe.hanoi.HanoiEntity;

public class LinearTwinHanoiEntity extends HanoiEntity {
	
	public void move(int from, int to) {
		if ((from == 0 && to == 2) || (from == 2 && to == 0)) 
			throw new RuntimeException(getGame().i18n.tr(
					"Sorry Dave, I cannot let you use move disks between slots 0 and 2 directly. Use the intermediate slot in all moves."));
		super.move(from,to);
	}
	

	public void run() {
		solve((Integer)getParam(0),(Integer) getParam(1),(Integer) getParam(2));
	}

	public void solve(int src, int other, int dst) {
		linearTwinHanoi(getSlotSize(src), src, other, dst);
	}

	/* BEGIN TEMPLATE */
	public void linearTwinHanoi(int height, int src, int mid, int dst) {
		/* BEGIN SOLUTION */
		gather(height-1,src,mid,dst);
//		System.err.println("End gather");
		move(src,mid);
		moveDouble(height-1, dst, mid, src);
		move(dst,mid);
		moveDouble(height-1, src, mid, dst);
		move(mid, src);
		moveDouble(height-1, dst, mid, src);
		move(mid, dst);
		scatter(height-1, src, mid, dst);
	}
	private void gather(int height, int src, int mid, int dst) {
		if (height >0) {
//			System.err.println("Gather("+height+")");
			gather(height-1,src,mid,dst);
			move(src,mid);
			moveDouble(height-1, dst,mid,src);
			move(mid,dst);
			moveDouble(height-1, src, mid, dst);
//			System.err.println("End gather("+height+")");
		}
	}
	private void scatter(int height, int src, int mid, int dst) {
		if (height>0) {
//			System.err.println("Scatter("+height+")");
			moveDouble(height-1, src, mid, dst);
			move(src,mid);
			moveDouble(height-1, dst, mid, src);
			move(mid,dst);
			scatter(height-1,src,mid,dst);
//			System.err.println("End scatter("+height+")");
		}
	}
	private void moveDouble(int height, int src, int mid, int dst) {
		if (height>0) {
//			System.err.println("moveDouble("+height+","+src+","+dst+")");
			moveDouble(height-1, src, mid, dst);
			move(src,mid);
			move(src,mid);
			moveDouble(height-1, dst, mid, src);
			move(mid,dst);
			move(mid,dst);
			moveDouble(height-1, src, mid, dst);
		}
		/* END SOLUTION */
	}

	/* END TEMPLATE */

}
