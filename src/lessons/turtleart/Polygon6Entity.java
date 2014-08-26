package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class Polygon6Entity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
	    addSizeHint(65,110, 65,190);

	    for (int i=0; i<6;i++) {
	        forward(80);
	        right(360/6);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
