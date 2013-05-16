package lessons.recursion.star;

import java.awt.Color;

import jlm.universe.turtles.Turtle;

public class StarEntity extends Turtle {

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	public void branch(int size) {
		forward(size);
		turnRight(360 / BRANCH_COUNT);
		forward(size);

		for (int i = 0; i < 2; i++)
			turnLeft(360 / BRANCH_COUNT);
	}

	public void star(int size, Color c) {
		setColor(c);
		for (int i = 0; i < BRANCH_COUNT; i++) {
			branch(size);
		}
	}

	public static final int BRANCH_COUNT = 5;

	public void run() {
		star(100, Color.black);
		turnRight(45);
		star(80, Color.blue);
		turnRight(45);
		star(60, Color.red);
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
