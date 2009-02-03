package lessons.welcome;

public class SpiralEntity extends universe.turtles.Turtle {

	/* BEGIN SOLUTION */
	public void run() {
		for (int i = 0; i < 150; i++) {
			if (i % 2 == 0) {
				setColor(java.awt.Color.blue);
			} else {
				setColor(java.awt.Color.red);
			}
			setHeading(i * 88);
			forward(i * 1.8);
		}
	}
	/* END TEMPLATE */
}
