package lessons.recursion;

import java.awt.Color;

public class DragonCurve2Entity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
public void dragon(int ordre, double x, double y, double z, double t) {
		/* BEGIN HIDDEN */
		double u, v;

		if (ordre == 1) {
			setColor(Color.red);
			moveTo(z, t);
		} else {
			u = (x + z + t - y) / 2;
			v = (y + t - z + x) / 2;
			dragon(ordre - 1, x, y, u, v);
			dragonInverse(ordre - 1, u, v, z, t);
		}
		/* END HIDDEN */
}

public void dragonInverse(int ordre, double x, double y, double z, double t) {
		/* BEGIN HIDDEN */
		double u, v;

		if (ordre == 1) {
			setColor(Color.blue);
			moveTo(z, t);
		} else {
			u = (x + z - t + y) / 2;
			v = (y + t + z - x) / 2;
			dragon(ordre - 1, x, y, u, v);
			dragonInverse(ordre - 1, u, v, z, t);
		}
		/* END HIDDEN */
}
	/* END TEMPLATE */

	public void run() {
		dragon((Integer) getParam(0), (Double) getParam(1), (Double) getParam(2), (Double) getParam(3),
				(Double) getParam(4));
	}

}
