package lessons.turtleart;

import plm.universe.turtles.Turtle;

/* Item 36 of billkerr2.blogspot.fr/2009/08/40-maths-shapes-challenges.html */

public class Kerr36Entity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		for(int i=0;i<8;i++) {
			branch(20);
			right(45);
		}
		addSizeHint(130, 85, 150, 85);
		addSizeHint(190, 115, 190, 135);

	}
	public void branch(int length) {
		forward(length);
		left(45);
		forward(length);
		right(90);
		forward(length);
		left(45);
		forward(length);

		left(90);
		forward(length);
		right(45);
		forward(length);
		right(135);
		forward(length);
		left(45);
		forward(length);

		right(90);
		forward(length);
		left(45);
		forward(length);
		right(135);
		forward(length);
		right(45);
		forward(length);

		left(90);
		forward(length);
		left(45);
		forward(length);
		right(90);
		forward(length);
		left(45);
		forward(length);
		right(180);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
