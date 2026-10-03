package lessons.turtleart;

import plm.universe.turtles.Turtle;

class Polygon7Entity extends Turtle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		addSizeHint(52,110, 52,190);
		
	    for (i <- 1 to 7) {
	        forward(80);
	        right(360.0/7.0);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
