package lessons.recursion;


public class CircleEntity extends jlm.universe.turtles.Turtle {

	/* BEGIN SOLUTION */
	public void circle(double step) {
		for (int i=0;i<360;i++) {
			forward(step);
			turnRight(1);
		}		
	}
	public void run() {
		circle(0.5);
		circle(1);
		circle(1.5);
	}
	/* END TEMPLATE */
}
