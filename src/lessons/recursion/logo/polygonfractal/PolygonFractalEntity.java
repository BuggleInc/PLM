package lessons.recursion.logo.polygonfractal;

import plm.universe.turtles.Turtle;

public class PolygonFractalEntity extends Turtle {

	/* BEGIN TEMPLATE */
	void polygonFractal (int levels, int sides, double length, double shrink) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			/* do nothing */
		} else {
			for (int i=0;i<sides;i++) {
				forward(length);

				left((sides-2)*360/(sides*2));
				polygonFractal(levels-1, sides, length*shrink,shrink);
				right((sides-2)*360/(sides*2));
				right(360/sides);

			}
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	public void run() {
		polygonFractal((Integer)getParam(0),(Integer)getParam(1),(Double)getParam(2),(Double)getParam(3));
	}
}
