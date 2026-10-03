package lessons.turtleart;

import plm.universe.turtles.Turtle;

class CircleTwoEntity extends Turtle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		for (i <- 1 to 360) {
			forward(1.0);
			right(1);
		}
		for (i <- 1 to 360) {
			forward(2);
			right(1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
