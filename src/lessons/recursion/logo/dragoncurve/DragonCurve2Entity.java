package lessons.recursion.logo.dragoncurve;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class DragonCurve2Entity extends Turtle {

	/* BEGIN TEMPLATE */
	public void dragon(int order, double x, double y, double z, double t) {
		/* BEGIN HIDDEN */
		double u, v;

		if (order == 1) {
			setColor(Color.red);
			moveTo(z, t);
		} else {
			u = (x + z + t - y) / 2;
			v = (y + t - z + x) / 2;
			dragon(order - 1, x, y, u, v);
			dragonInverse(order - 1, u, v, z, t);
		}
		/* END HIDDEN */
	}

	public void dragonInverse(int order, double x, double y, double z, double t) {
		/* BEGIN HIDDEN */
		double u, v;

		if (order == 1) {
			setColor(Color.blue);
			moveTo(z, t);
		} else {
			u = (x + z - t + y) / 2;
			v = (y + t + z - x) / 2;
			dragon(order - 1, x, y, u, v);
			dragonInverse(order - 1, u, v, z, t);
		}
		/* END HIDDEN */
	}
	/* END TEMPLATE */

	public void run() {
		dragon((Integer) getParam(0), (Double) getParam(1), (Double) getParam(2), (Double) getParam(3),
				(Double) getParam(4));
	}

}
