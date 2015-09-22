package lessons.recursion.logo.koch;

import plm.universe.turtles.Turtle;

public class HexaKochEntity extends Turtle {

	/* BEGIN TEMPLATE */
	void drawCurve(int levels, double length) {
		hexaKoch(levels, length);
	}
	void hexaKoch(int levels, double length) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			hexaKoch(levels-1, length*.14);
			left(120);
			for (int i=0;i<5;i++) {
				hexaKoch(levels-1, length*.14);
				right(60);
			}
			left(180);
			hexaKoch(levels-1, length*.14);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	public void run() {
		drawCurve((Integer)getParam(0),(Double)getParam(1));
	}
}
