package lessons.sort.baseball;

import lessons.sort.baseball.universe.BaseballEntity;
import lessons.sort.baseball.universe.BaseballWorld;

class SelectBaseballEntity extends BaseballEntity {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		for (base <- 0 to getBasesAmount() -2) 
			bringPlayersHome(base);
		
		assertSorted("selection sort");
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END HIDDEN */
}
