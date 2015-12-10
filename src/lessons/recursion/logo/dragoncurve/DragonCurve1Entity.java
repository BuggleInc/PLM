package lessons.recursion.logo.dragoncurve;

import plm.universe.turtles.Turtle;

public class DragonCurve1Entity extends Turtle {

	/* BEGIN TEMPLATE */
	public void dragon(int order, double x, double y, double z, double t) {
		/* BEGIN SOLUTION */
		double u, v;

		if (order == 1) {
			setPos(x, y);
			moveTo(z, t);
		} else {
			u = (x + z + t - y) / 2;
			v = (y + t - z + x) / 2;
			dragon(order - 1, x, y, u, v);
			dragon(order - 1, z, t, u, v);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	public void run() {
		dragon((Integer) getParam(0), (Double) getParam(1), (Double) getParam(2), (Double) getParam(3),
				(Double) getParam(4));
	}

}
