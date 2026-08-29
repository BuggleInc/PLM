package lessons.welcome.instructions;

import plm.universe.bugglequest.SimpleBuggle;

class InstructionsEntity extends SimpleBuggle {

	override def run() {
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		brushDown();
		for (i <- 0 to 3) {
			forward(2);
			right();
		}
		brushUp();
		stepForward();
		right();
		stepForward();
		left();
		/* END SOLUTION */
		/* END TEMPLATE */
	}

}
