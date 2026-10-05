package lessons.recursion.lego.sierpinski;

class SierpinskiEntity extends plm.universe.turtles.Turtle {
	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	def sierpinski(level:Int, length:Double): Unit = {
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

	override def run(): Unit = {
		sierpinski(getParamInt(0), getParamDouble(1));
	}
	/* END REMOTE */

}
