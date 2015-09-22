package lessons.recursion.logo.koch;

import plm.universe.turtles.Turtle;
import java.awt.Color

class ScalaKochEntity extends Turtle {

	/* BEGIN TEMPLATE */
	def snowFlake (levels:Int, length:Double) {
		snowSide(levels, length);
		right(120);
		setColor(Color.blue);
		snowSide(levels, length);
		right(120);
		setColor(Color.orange);
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
