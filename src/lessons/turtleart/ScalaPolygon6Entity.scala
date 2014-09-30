package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaPolygon6Entity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
	    addSizeHint(65,110, 65,190);

	    for (i <- 1 to 6) {
	        forward(80);
	        right(360/6);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
