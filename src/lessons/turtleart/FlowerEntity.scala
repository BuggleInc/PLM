package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaFlowerEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */

		addSizeHint(80, 175, 80, 125)

		for(i <- 1 to 8) {
			forward(50.0)
			right(360/8)
		}
		forward(25.0)
		left(360/8)
		for(i <- 1 to 8) {
			right(360/4)
			forward(25.0)
			left(360/8)
			forward(25.0)
		}
		right(360/4)
		forward(25.0)
		left(360/4)
		for(i <- 1 to 8) {
			right(3*360/8)
			forward(25.0)
			right(360/8)
			forward(25.0)
			backward(25.0)
			left(3*360/8)
			forward(25.0)
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
