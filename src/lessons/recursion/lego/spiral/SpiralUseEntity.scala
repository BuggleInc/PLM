package lessons.recursion.lego.spiral;

import plm.universe.turtles.Turtle;

class SpiralUseEntity extends Turtle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	def spiral(steps:Int, angle:Int, length:Int, increment:Int): Unit = {
		if (steps > 0) {
			forward(length);
			left(angle);
			spiral(steps-1, angle, length+increment, increment);
		}
		/* BEGIN SOLUTION */
		// Nothing to hide: the template is the solution, but the templating mechanism expects a SOLUTION section
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(): Unit = spiral(100,91,1,2);
	/* END REMOTE */
}
