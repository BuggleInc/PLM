package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaCircleSquareEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
        addSizeHint(35,100, 35,200);

        for (i <- 1 to 4) {
        	forward(100);
        	right(90);
        }
        forward(100);
        right(90);
        forward(50);
        circle(50);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
