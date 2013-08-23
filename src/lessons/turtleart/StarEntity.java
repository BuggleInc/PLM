package lessons.turtleart;

import jlm.universe.turtles.Turtle;

public class StarEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		for (int i = 0; i < BRANCH_COUNT; i++) 
			branch(50);
		
	}
	static final int BRANCH_COUNT = 5;
	public void branch(int size) {
		forward(size);
		right(360 / BRANCH_COUNT);
		forward(size);

		for (int i = 0; i < 2; i++)
			left(360 / BRANCH_COUNT);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
