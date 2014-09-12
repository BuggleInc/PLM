package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class Polygon20Entity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
	    addSizeHint(45,135, 45,165);
	    int sides=20;

	    for (int i=0; i<sides;i++) {
	        forward(30);
	        right(360/sides);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
