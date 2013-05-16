package lessons.recursion.spiral;

public class SpiralEntity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
public void spiral(int steps, int angle, int length, int increment)	{
		/* BEGIN SOLUTION */
	     if (steps <= 0) {
	          // do nothing
	     } else {
	          forward(length);
	          turnLeft(angle);
	          spiral(steps-1, angle, length+increment, increment);
	     }
	     /* END SOLUTION */	
}
	/* END TEMPLATE */

	public void run() {
		spiral((Integer)getParam(0),(Integer)getParam(1),(Integer)getParam(2),(Integer)getParam(3));
	}
}
