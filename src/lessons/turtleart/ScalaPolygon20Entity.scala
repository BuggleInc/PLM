package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaPolygon20Entity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
	    addSizeHint(45,135, 45,165);
	    val sides=20;

	    for (i <- 1 to sides) {
	        forward(30);
	        right(360/sides);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
