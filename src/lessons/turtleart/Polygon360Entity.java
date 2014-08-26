package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class Polygon360Entity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		addSizeHint(15,149, 15,151);

		for (int i=0; i<360;i++) {
			forward(2);
			right(1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
