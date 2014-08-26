package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class TriangleFlatEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
        addSizeHint(35,50, 35,250);

        for (int i = 0; i < 3; i++) {
        	forward(200);
        	right(120);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
