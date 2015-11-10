package lessons.recursion.logo.square;

import plm.universe.turtles.Turtle;

public class FourSquareEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
        addSizeHint(90,100, 90,200);

		for (int i = 0; i < 4; i++) {
			square();
			right(90);
		}
	}
	public void square() {
		for (int i = 0; i < 4; i++) {
			forward(100);
			right(90);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
