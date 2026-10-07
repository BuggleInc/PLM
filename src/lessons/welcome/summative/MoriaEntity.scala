package lessons.welcome.summative;

import plm.universe.bugglequest.SimpleBuggle;

class MoriaEntity extends SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		back();
		while (!isFacingWall()) {
			while (!isOverBaggle() && !isFacingWall())
				stepForward();
			if (isOverBaggle()) {
				pickupBaggle();
				back();
				while (!isOverBaggle())
					stepForward();
				stepBackward();
				dropBaggle();
				back();
				stepForward();
			}
		}
		right();
		stepForward();
		left();
		stepForward();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
