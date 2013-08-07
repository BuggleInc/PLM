package lessons.welcome.basics;

import jlm.universe.bugglequest.SimpleBuggle;

class ScalaBasicsEntity extends SimpleBuggle {

	protected def run() {
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		brushDown();
		for (i <- 0 to 3) {
			forward(2);
			turnRight();
		}
		brushUp();
		forward();
		turnRight();
		forward();
		turnLeft();
		/* END SOLUTION */
		/* END TEMPLATE */
	}

}
