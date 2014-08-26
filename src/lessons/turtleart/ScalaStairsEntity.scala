package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaStairsEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
        addSizeHint(50,170, 85,170);

        for (i <- 1 to 6) {
        	forward(35);
        	right(90);
            forward(35);
            left(90);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
