package lessons.recursion.logo.koch;

import plm.universe.turtles.Turtle;
import java.awt.Color

class ScalaSquareKochEntity extends Turtle {

	/* BEGIN TEMPLATE */
	def snowSquare (levels:Int, length:Double) {
		squareSide(levels, length);
		right(90);
		setColor(Color.blue);
		squareSide(levels, length);
		right(90);
		setColor(Color.orange);
		squareSide(levels, length);
		right(90);
		setColor(Color.magenta);
		squareSide(levels, length);
		right(90);
	}
	def squareSide(levels:Int, length:Double) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			squareSide(levels-1, length/3);
			left(90);
			squareSide(levels-1, length/3);
			right(90);
			squareSide(levels-1, length/3);
			right(90);
			squareSide(levels-1, length/3);
			left(90);
			squareSide(levels-1, length/3);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	override def run() {
		snowSquare(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Double]);
	}
}
