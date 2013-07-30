package lessons.sort.baseball;

import lessons.sort.baseball.universe.BaseballEntity;

public class BubbleBaseballEntity extends BaseballEntity {
	
	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		while (!isSorted()) {
			while (getHoleBase()>0) {
				int maxPos = 0;
				int maxColor = getPlayerColor(getHoleBase()-1, 0);
				for (int pos=1;pos<getPositionsAmount();pos++) 
					if (getPlayerColor(getHoleBase()-1, pos) > maxColor) {
						maxColor = getPlayerColor(getHoleBase()-1, pos);
						maxPos = pos;
					}
				move(getHoleBase()-1,maxPos);
			}
			while (getHoleBase()<getBasesAmount()-1) {
				int minPos = 0;
				int minColor = getPlayerColor(getHoleBase()+1, 0);
				for (int pos=1;pos<getPositionsAmount();pos++) 
					if (getPlayerColor(getHoleBase()+1, pos) < minColor) {
						minColor = getPlayerColor(getHoleBase()+1, pos);
						minPos = pos;
					}
				move(getHoleBase()+1,minPos);				
			}
		}
		if (!isSorted())
			System.out.println("IT'S STILL NOT SORTED!! PLEASE REPORT THIS BUG"+world.toString());
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}