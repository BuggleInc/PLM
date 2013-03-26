package lessons.recursion;

public class TreeEntity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
public void tree(int steps, double length, double angle, double shrink)	{
		/* BEGIN SOLUTION */
	     if (steps <= 0) {
	          /* do nothing */
	     } else {
	          forward(length);
	          turnRight(angle);	         
	          tree(steps-1, length*shrink, angle, shrink);
	          turnLeft(2*angle);	         
	          tree(steps-1, length*shrink, angle, shrink);
	          turnRight(angle);	         
	          backward(length);
	     }
	     /* END SOLUTION */	
}
	/* END TEMPLATE */

	public void run() {
		tree((Integer)getParam(0),(Double)getParam(1),(Double)getParam(2),(Double)getParam(3));
	}
}
