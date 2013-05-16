package lessons.recursion.square;

import jlm.universe.turtles.Turtle;

public class SquareEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		for (int i = 0; i < 4; i++) {
			square();
			turnRight(90);
		}
	}
	public void square() {
		for (int i = 0; i < 4; i++) {
			forward(100);
			turnRight(90);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
