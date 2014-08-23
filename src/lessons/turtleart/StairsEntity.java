package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class StairsEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
        addSizeHint(50,170, 85,170);

        for (int i = 0; i < 6; i++) {
        	forward(35);
        	right(90);
            forward(35);
            left(90);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
