package lessons.recursion.lego.spiral;

class SpiralEntity extends plm.universe.turtles.Turtle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	def spiral(steps:Int, angle:Int, length:Int, increment:Int): Unit = {
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

	override def run(): Unit = {
		spiral(getParamInt(0),getParamInt(1),
		    getParamInt(2),getParamInt(3));
	}
	/* END REMOTE */
}
