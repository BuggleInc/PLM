package lessons.turtleart;

import plm.universe.turtles.Turtle;

/* Original creation of one of our students */

class ScalaFlower3Entity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run():Unit= {
		/* BEGIN SOLUTION */
		val angle = 45;
		addSizeHint(155,102,155,122);
		for(i <- 1 to (360/angle)) {
			right(45.0);
			forward(20.0);
			left(90.0);
			forward(20.0);
			right(45.0);
			forward(20.0);
			right(90.0);
			for (j <- 1 to 2) {
				forward(20.0);
				left(90.0);
				forward(20.0);
				left(90.0);
				forward(20.0);
			}
			right(90.0);
			forward(20.0);
			right(45.0);
			forward(20.0);
			left(90.0);
			forward(20.0);
			right(45.0);
			right(180.0);
			right(angle);
		}
		hide();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
