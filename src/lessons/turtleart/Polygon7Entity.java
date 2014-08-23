package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class Polygon7Entity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		addSizeHint(52,110, 52,190);
		
	    for (int i=0; i<7;i++) {
	        forward(80);
	        right(360./7.);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
