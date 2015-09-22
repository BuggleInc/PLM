package lessons.recursion.logo.koch;

import plm.universe.turtles.Turtle;
import java.awt.Color

class ScalaCrabEntity extends Turtle {

	/* BEGIN TEMPLATE */
	def crab(levels:Int, length:Double) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			left(45);
			crab(levels-1, length/Math.sqrt(2));
			right(90);
			crab(levels-1, length/Math.sqrt(2));
			left(45);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	override def run() {
		crab(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Double]);
	}
}
