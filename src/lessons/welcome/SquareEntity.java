package lessons.welcome;


public class SquareEntity extends universe.turtles.Turtle {

	/* BEGIN SOLUTION */
	public void square() {
		for (int i=0;i<4;i++) {
			forward(100.);
			turnRight(90.);
		}		
	}
	public void run() {
		for (int i=0;i<4;i++) {
			square();
			turnRight(90);
		}
	}
	/* END TEMPLATE */
}
