package lessons.recursion.logo.koch;

import plm.universe.turtles.Turtle;
import java.awt.Color

class ScalaPentaKochEntity extends Turtle {

	/* BEGIN TEMPLATE */
	def pentaKoch(levels:Int, length:Double) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			pentaKoch(levels-1, length*.4);
			left(108);
			pentaKoch(levels-1, length*.4);
			right(72);
			pentaKoch(levels-1, length*.4);
			right(72);
			pentaKoch(levels-1, length*.4);
			right(72);
			pentaKoch(levels-1, length*.4);
			left(108);
			pentaKoch(levels-1, length*.4);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	override def run() {
		pentaKoch(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Double]);
	}
}
