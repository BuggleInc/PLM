package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class FlowerEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		addSizeHint(80, 175, 80, 125);

		for(int i=0;i<8;i++) {
			forward(50.0);
			right(360/8);
		}
		forward(25.0);
		left(360/8);
		for(int i=0;i<8;i++) {
			right(360/4);
			forward(25.0);
			left(360/8);
			forward(25.0);
		}
		right(360/4);
		forward(25.0);
		left(360/4);
		for(int i=0;i<8;i++) {
			right(3*360/8);
			forward(25.0);
			right(360/8);
			forward(25.0);
			backward(25.0);
			left(3*360/8);
			forward(25.0);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
