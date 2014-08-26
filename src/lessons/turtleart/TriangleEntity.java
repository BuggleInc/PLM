package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class TriangleEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
        addSizeHint(50,265, 250,265);
        
        right(30);
        for (int i = 0; i < 3; i++) {
        	forward(200);
        	right(120);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
