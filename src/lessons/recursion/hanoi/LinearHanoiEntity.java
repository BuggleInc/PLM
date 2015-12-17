package lessons.recursion.hanoi;

import plm.universe.hanoi.HanoiEntity;

public class LinearHanoiEntity extends HanoiEntity {

	public void move(int from, int to) {
		if ((from == 0 && to == 2) || (from == 2 && to == 0)) 
			throw new RuntimeException(getGame().i18n.tr(
					"Sorry Dave, I cannot let you use move disks between slots 0 and 2 directly. Use the intermediate slot in all moves."));
		super.move(from,to);
	}
	
	public void run() {
		linearHanoi(getSlotSize((Integer)getParam(0)), (Integer)getParam(0), (Integer)getParam(1),(Integer)getParam(2));
	}

	/* BEGIN TEMPLATE */
	public void linearHanoi(int height, int src, int mid, int dst) {
		/* BEGIN SOLUTION */
		if (height != 0) {
			linearHanoi(height-1, src, mid, dst);
			move(src,mid);
			linearHanoi(height-1, dst,mid,src);
			move(mid,dst);
			linearHanoi(height-1, src, mid, dst);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
