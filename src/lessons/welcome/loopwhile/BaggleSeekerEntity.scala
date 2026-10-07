package lessons.welcome.loopwhile;

import plm.universe.bugglequest.SimpleBuggle;

class BaggleSeekerEntity extends SimpleBuggle {


	/* BEGIN REMOTE */
	override def run(): Unit = { 
		/* BEGIN SOLUTION */
		while (!isOverBaggle()) {
			stepForward();
		}
		/* END SOLUTION */
	}
	/* END REMOTE */
}
