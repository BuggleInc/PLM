package lessons.recursion.spiral;

class ScalaSpiralEntity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
	def spiral(steps:Int, angle:Int, length:Int, increment:Int)	{
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

	override def run() {
		spiral(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Int],
		    getParam(2).asInstanceOf[Int],getParam(3).asInstanceOf[Int]);
	}
}
