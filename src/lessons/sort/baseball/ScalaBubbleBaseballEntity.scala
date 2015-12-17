package lessons.sort.baseball;

import plm.universe.baseball.BaseballWorld
import plm.universe.baseball.BaseballEntity

class ScalaBubbleBaseballEntity extends BaseballEntity {
	
	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		while (!isSorted()) {
			while (getHoleBase()>0) {
				var maxPos = 0;
				var maxColor = getPlayerColor(getHoleBase()-1, 0);
				for (pos <- 1 to getPositionsAmount()-1) 
					if (getPlayerColor(getHoleBase()-1, pos) > maxColor) {
						maxColor = getPlayerColor(getHoleBase()-1, pos);
						maxPos = pos;
					}
				move(getHoleBase()-1,maxPos);
			}
			while (getHoleBase()<getBasesAmount()-1) {
				var minPos = 0;
				var minColor = getPlayerColor(getHoleBase()+1, 0);
				for (pos <- 1 to getPositionsAmount()-1) 
					if (getPlayerColor(getHoleBase()+1, pos) < minColor) {
						minColor = getPlayerColor(getHoleBase()+1, pos);
						minPos = pos;
					}
				move(getHoleBase()+1,minPos);				
			}
		}
		world.asInstanceOf[BaseballWorld].assertSorted("bubble sort");
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}