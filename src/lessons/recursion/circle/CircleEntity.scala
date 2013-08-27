package lessons.recursion.circle;

import plm.universe.turtles.Turtle;

class ScalaCircleEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		circle(0.5);
		circle(1);
		circle(1.5);
	}
	override def circle(step:Double) {
		for (i <- 1 to 360) {
			forward(step);
			right(1);
		}		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
