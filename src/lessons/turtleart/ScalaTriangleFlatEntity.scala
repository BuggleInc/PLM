package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaTriangleFlatEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
        addSizeHint(35,50, 35,250);

        for (i <- 1 to 3) {
        	forward(200);
        	right(120);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
