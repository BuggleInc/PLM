package lessons.recursion;

public class PolygonFractalEntity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
void polygonFractal (int levels, int sides, double length, double shrink) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			/* do nothing */
		} else {
			for (int i=0;i<sides;i++) {
				forward(length);
				
				turnLeft((sides-2)*360/(sides*2));
				polygonFractal(levels-1, sides, length*shrink,shrink);
				turnRight((sides-2)*360/(sides*2));
				turnRight(360/sides);
				
			}
		}
		/* END SOLUTION */	
}
	/* END TEMPLATE */

	public void run() {
		polygonFractal((Integer)getParam(0),(Integer)getParam(1),(Double)getParam(2),(Double)getParam(3));
	}
}
