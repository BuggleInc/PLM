package lessons.recursion.logo.koch;

import plm.universe.turtles.Turtle;

public class CrabEntity extends Turtle {

	/* BEGIN TEMPLATE */
	void crab(int levels, double length) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			left(45);
			crab(levels-1, length/Math.sqrt(2));
			right(90);
			crab(levels-1, length/Math.sqrt(2));
			left(45);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	public void run() {
		crab((Integer)getParam(0),(Double)getParam(1));
	}
}
