package lessons.recursion.koch;

import jlm.universe.turtles.Turtle;

class ScalaKochEntity extends Turtle {

	/* BEGIN TEMPLATE */
	def snowFlake (levels:Int, length:Double) {
		snowSide(levels, length);
		right(120);
		snowSide(levels, length);
		right(120);
		snowSide(levels, length);
		right(120);
	}
	def snowSide(levels:Int, length:Double) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			snowSide(levels-1, length/3);
			left(60);
			snowSide(levels-1, length/3);
			right(120);
			snowSide(levels-1, length/3);
			left(60);
			snowSide(levels-1, length/3);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	override def run() {
		snowFlake(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Double]);
	}
}
