package lessons.recursion.circle;

import jlm.universe.turtles.Turtle;

public class CircleEntity extends Turtle {

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	public void circle(double step) {
		for (int i=0;i<360;i++) {
			forward(step);
			turnRight(1);
		}		
	}
	public void run() {
		circle(0.5);
		circle(1);
		circle(1.5);
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
