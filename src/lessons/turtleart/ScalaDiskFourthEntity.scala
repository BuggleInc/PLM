package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaDiskFourthEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		addSizeHint(135, 150, 135, 50);
	    for (i <- 1 to 90) {
	        forward(100);
	        backward(100);
	        right(1);
	    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
