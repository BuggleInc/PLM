package lessons.turtleart;

import plm.universe.turtles.Turtle;

class TriangleFlatEntity extends Turtle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
        addSizeHint(35,50, 35,250);

        for (i <- 1 to 3) {
        	forward(200);
        	right(120);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
