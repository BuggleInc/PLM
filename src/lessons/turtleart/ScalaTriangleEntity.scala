package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaTriangleEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
        addSizeHint(50,265, 250,265);
        
        right(30);
        for (i <- 1 to 3) {
        	forward(200);
        	right(120);
        }
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
