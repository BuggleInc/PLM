package lessons.recursion.hanoi;

import plm.universe.hanoi.HanoiEntity;

public class IterativeHanoiEntity extends HanoiEntity {
	
	public void run() {
		hanoi((Integer)getParam(0),(Boolean) getParam(1));
	}

	/* BEGIN TEMPLATE */
	void hanoi(int initialPos, boolean increasing) {
		/* BEGIN SOLUTION */
		int small = initialPos;
		int count = 0;
		int pos1=0,pos2=0;
		do {
			if (count%2 == 0) {
				int next = (small+3+(increasing?1:-1)) % 3;
				//getGame().getLogger().log("move("+small+","+next+")");
				move(small, next);
				small=next;
			}
			
			switch (small) {
			case 0: pos1=1; pos2=2; break;
			case 1: pos1=0; pos2=2; break;
			case 2: pos1=0; pos2=1; break;
			}
			if (count%2 == 1) {
				if (getSlotRadius(pos1) > getSlotRadius(pos2))
					move(pos2,pos1);
				else
					move(pos1,pos2);
			}
			
			count++;
		} while (getSlotSize(pos1) != 0 || getSlotSize(pos2) != 0);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
