package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class CircleYingEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
	    for (int i=0; i<360;i++) {
	        forward(2);
	        right(1);
	    }
	    for (int i=0; i<180;i++) {
	        forward(1);
	        right(1);
	    }
	    for (int i=0; i<180;i++) {
	        forward(1);
	        left(1);
	    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
