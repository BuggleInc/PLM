package lessons.recursion.logo.spiral;

import plm.universe.turtles.Turtle;

public class SpiralUseEntity extends Turtle {

	public void spiral(int steps, int angle, int length, int increment)	{
		if (steps <= 0) {
			return;
		} else {
			forward(length);
			left(angle);
			spiral(steps-1, angle, length+increment, increment);
		}
	}

	/* BEGIN TEMPLATE */
	public void run() {
		spiral(100,91,1,2);
	}
	/* END TEMPLATE */
}
