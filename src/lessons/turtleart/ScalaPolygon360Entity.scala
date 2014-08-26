package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaPolygon360Entity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		addSizeHint(15,149, 15,151);

		for (i <- 1 to 360) {
			forward(2);
			right(1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
