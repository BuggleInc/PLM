//RemoteTurtle

/* BEGIN TEMPLATE */
void dragon(int order, double x, double y, double z, double t) {
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

void run() {
	dragon(getParamInt(0),  getParamDouble(1),  getParamDouble(2), getParamDouble(3), getParamDouble(4));
}
