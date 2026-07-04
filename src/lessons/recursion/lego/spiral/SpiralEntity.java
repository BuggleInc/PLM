package lessons.recursion.lego.spiral;

public class SpiralEntity extends plm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
	public void spiral(int steps, int angle, int length, int increment)	{
		/* BEGIN SOLUTION */
		if (steps <= 0) {
			// do nothing
		} else {
			forward(length);
			left(angle);
			spiral(steps-1, angle, length+increment, increment);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	public void run() {
		spiral(getParamInt(0),getParamInt(1),getParamInt(2),getParamInt(3));
	}
}
