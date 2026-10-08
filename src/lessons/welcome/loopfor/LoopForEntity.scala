package lessons.welcome.loopfor;

import plm.universe.bugglequest.SimpleBuggle

class LoopForEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var cpt = 0
		while (!isOverBaggle()) {
			cpt+=1;
			stepForward();
		}
		pickupBaggle();
		for (cpt2 <- 0  to cpt-1) {
			stepBackward();
		}
		dropBaggle();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
