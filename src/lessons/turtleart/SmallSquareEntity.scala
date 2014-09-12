package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaSmallSquareEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
        addSizeHint(35,50, 35,150);
        addSizeHint(150,35, 50,35);

        for (i <- 1 to 4) {
        	forward(100);
        	right(90);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
