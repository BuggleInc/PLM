package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class DiskFourthEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		addSizeHint(135, 150, 135, 50);
	    for (int i=0; i<90;i++) {
	        forward(100);
	        backward(100);
	        right(1);
	    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
