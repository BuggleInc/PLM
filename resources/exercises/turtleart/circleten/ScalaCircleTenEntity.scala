package turtleart;

import plm.universe.turtles.Turtle;

class ScalaCircleTenEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
	    for (i <- 10 to 100 by 10) {
	        circle(i.toDouble);
	    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
