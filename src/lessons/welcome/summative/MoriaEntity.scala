package lessons.welcome.summative;

import plm.universe.bugglequest.SimpleBuggle;

class MoriaEntity extends SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		back();
		while (!isFacingWall()) {
			while (!isOverBaggle() && !isFacingWall())
				step();
			if (isOverBaggle()) {
				pickupBaggle();
				back();
				while (!isOverBaggle())
					step();
				stepBack();
				dropBaggle();
				back();
				step();
			}
		}
		right();
		step();
		left();
		step();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
