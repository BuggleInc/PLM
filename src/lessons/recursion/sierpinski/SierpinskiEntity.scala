package lessons.recursion.sierpinski;

class ScalaSierpinskiEntity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
	def sierpinski(level:Int, length:Double) {
		/* BEGIN SOLUTION */
		if (level >= 0) {
			for (i <- 1 to 3) {
				forward(length / 2.);
				right(360. / 3.);
				sierpinski(level-1, length / 2.);
				left(360. / 3.);
				forward(length / 2.);
				right(360. / 3.);
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() {
		sierpinski(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Double]);
	}

}
