package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class SquareEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
        addSizeHint(35,50, 35,250);
        addSizeHint(250,35, 50,35);

        for (int i = 0; i < 4; i++) {
        	forward(200);
        	right(90);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
