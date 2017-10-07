package turtleart.circletwo;

import plm.universe.turtles.Turtle;

public class CircleTwoEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		for (int i=0; i<360;i++) {
			forward(1.);
			right(1);
		}
		for (int i=0; i<360;i++) {
			forward(2);
			right(1);
		}
		
		addSizeHint(165,150, 165,151);
		addSizeHint(280,149, 280,151);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
