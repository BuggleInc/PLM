package lessons.turtleart;

import jlm.universe.turtles.Turtle;

class ScalaPolygon7Entity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		addSizeHint(52,110, 52,190);
		
	    for (i <- 1 to 7) {
	        forward(80);
	        right(360./7.);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
