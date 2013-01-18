package lessons.recursion;

public class DragonCurve1Entity extends jlm.universe.turtles.Turtle {

	/* BEGIN TEMPLATE */
public void dragon(int ordre, double x, double y, double z, double t) {
		/* BEGIN SOLUTION */
		double u, v;

		if (ordre == 1) {
			setPos(x, y);
			moveTo(z, t);
		} else {
			u = (x + z + t - y) / 2;
			v = (y + t - z + x) / 2;
			dragon(ordre - 1, x, y, u, v);
			dragon(ordre - 1, z, t, u, v);
		}
		/* END SOLUTION */
}
	/* END TEMPLATE */

	public void run() {
		dragon((Integer) getParam(0), (Double) getParam(1), (Double) getParam(2), (Double) getParam(3),
				(Double) getParam(4));
	}

}
