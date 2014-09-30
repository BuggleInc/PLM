package lessons.turtleart;

import java.awt.Color;

import plm.universe.turtles.Turtle;

class ScalaDiskTwoEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		for (i <- 1  to 9) {
			setColor(Color.BLACK);
			quadrant();
			setColor(Color.RED);
			quadrant();
		}
	}
	def quadrant() {
		for (i <- 1 to 20) {
			forward(100);
			backward(100);
			right(1);
		}		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
