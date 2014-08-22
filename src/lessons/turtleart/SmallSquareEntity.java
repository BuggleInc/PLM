package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class SmallSquareEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
        addSizeHint(35,50, 35,150);
        addSizeHint(150,35, 50,35);

        for (int i = 0; i < 4; i++) {
        	forward(100);
        	right(90);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
