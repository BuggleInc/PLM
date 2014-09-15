package lessons.turtleart;

import java.awt.Color;

import plm.universe.turtles.Turtle;

class ScalaDiskFourEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		quadrant();
		setColor(Color.RED);
		quadrant();
		setColor(Color.ORANGE);
		quadrant();
		setColor(Color.MAGENTA);
		quadrant();
	}
	def quadrant() {
		for (i <- 1 to 90) {
			forward(100);
			backward(100);
			right(1);
		}		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
