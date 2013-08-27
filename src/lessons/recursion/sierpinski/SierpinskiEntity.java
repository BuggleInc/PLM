package lessons.recursion.sierpinski;

public class SierpinskiEntity extends plm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
	public void sierpinski(int level, double length) {
		/* BEGIN SOLUTION */
		if (level >= 0) {
			for (int i = 0; i < 3; i++) {
				forward(length / 2.);
				right(360. / 3.);
				sierpinski(level-1, length / 2.);
				left(360. / 3.);
				forward(length / 2.);
				right(360. / 3.);
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	public void run() {
		sierpinski((Integer) getParam(0), (Double) getParam(1));
	}

}
