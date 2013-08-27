package lessons.recursion.star;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class StarEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		star(100, Color.black);
		right(45);
		star(80, Color.blue);
		right(45);
		star(60, Color.red);
	}
	public void branch(int size) {
		forward(size);
		right(360 / BRANCH_COUNT);
		forward(size);

		for (int i = 0; i < 2; i++)
			left(360 / BRANCH_COUNT);
	}

	public static final int BRANCH_COUNT = 5;
	public void star(int size, Color c) {
		setColor(c);
		for (int i = 0; i < BRANCH_COUNT; i++) {
			branch(size);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
