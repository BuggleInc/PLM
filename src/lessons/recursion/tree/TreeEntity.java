package lessons.recursion.tree;

import plm.universe.turtles.Turtle;

public class TreeEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void tree(int steps, double length, double angle, double shrink)	{
		/* BEGIN SOLUTION */
		if (steps <= 0) {
			/* do nothing */
		} else {
			forward(length);
			right(angle);	         
			tree(steps-1, length*shrink, angle, shrink);
			left(2*angle);	         
			tree(steps-1, length*shrink, angle, shrink);
			right(angle);	         
			backward(length);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	public void run() {
		tree((Integer)getParam(0),(Double)getParam(1),(Double)getParam(2),(Double)getParam(3));
	}
}
