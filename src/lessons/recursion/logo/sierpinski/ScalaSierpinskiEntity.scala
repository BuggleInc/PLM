package lessons.recursion.logo.sierpinski;

class ScalaSierpinskiEntity extends plm.universe.turtles.Turtle {
	/* BEGIN TEMPLATE */
	def sierpinski(level:Int, length:Double) {
		/* BEGIN SOLUTION */
		if (level >= 0) {
			for (i <- 1 to 3) {
	             sierpinski(level-1,length/2);
	             forward(length);
	             right(120);
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() {
		sierpinski(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Double]);
	}

}
