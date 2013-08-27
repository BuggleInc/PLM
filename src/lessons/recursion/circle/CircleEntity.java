package lessons.recursion.circle;

import plm.universe.turtles.Turtle;

public class CircleEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		circle(0.5);
		circle(1);
		circle(1.5);
	}
	public void circle(double step) {
		for (int i=0;i<360;i++) {
			forward(step);
			right(1);
		}		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
