package lessons.recursion.logo.spiral;

import plm.universe.turtles.Turtle;

class ScalaSpiralUseEntity extends Turtle {

	def spiral(steps:Int, angle:Int, length:Int, increment:Int)	{
		if (steps <= 0) {
			return;
		} else {
			forward(length);
			left(angle);
			spiral(steps-1, angle, length+increment, increment);
		}
	}

	/* BEGIN TEMPLATE */
	override def run() {
		spiral(100,91,1,2);
	}
	/* END TEMPLATE */
}
