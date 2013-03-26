package lessons.recursion;

public class SierpinskiEntity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
public void sierpinski(int level, double length) {
		/* BEGIN SOLUTION */
		if (level >= 0) {
			for (int i = 0; i < 3; i++) {
				forward(length / 2.);
				turnRight(360. / 3.);
				sierpinski(level-1, length / 2.);
				turnLeft(360. / 3.);
				forward(length / 2.);
				turnRight(360. / 3.);
			}
		}
		/* END SOLUTION */
}
	/* END TEMPLATE */

	public void run() {
		sierpinski((Integer) getParam(0), (Double) getParam(1));
	}

}
