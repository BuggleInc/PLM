package lessons.recursion;

public class KochEntity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
void snowFlake (int levels, double length) {
	snowSide(levels, length);
	turnRight(120);
	snowSide(levels, length);
	turnRight(120);
	snowSide(levels, length);
	turnRight(120);
}
void snowSide(int levels, double length) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			snowSide(levels-1, length/3);
			turnLeft(60);
			snowSide(levels-1, length/3);
			turnRight(120);
			snowSide(levels-1, length/3);
			turnLeft(60);
			snowSide(levels-1, length/3);
		}
		/* END SOLUTION */	
}
	/* END TEMPLATE */

	public void run() {
		snowFlake((Integer)getParam(0),(Double)getParam(1));
	}
}
