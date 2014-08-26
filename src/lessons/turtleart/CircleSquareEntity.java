package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class CircleSquareEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
        addSizeHint(35,100, 35,200);

        for (int i = 0; i < 4; i++) {
        	forward(100);
        	right(90);
        }
        forward(100);
        right(90);
        forward(50);
        circle(50);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
