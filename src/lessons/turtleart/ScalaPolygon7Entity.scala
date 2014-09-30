package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaPolygon7Entity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		addSizeHint(52,110, 52,190);
		
	    for (i <- 1 to 7) {
	        forward(80);
	        right(360.0/7.0);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
