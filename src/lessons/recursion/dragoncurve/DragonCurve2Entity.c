//RemoteTurtle
void dragon(int order, double x, double y, double z, double t);
void dragonInverse(int order, double x, double y, double z, double t);
/* BEGIN TEMPLATE */
void dragon(int order, double x, double y, double z, double t) {
	/* BEGIN HIDDEN */
	double u, v;

	if (order == 1) {
		setColor(red);
		moveTo(z, t);
	} else {
		u = (x + z + t - y) / 2;
		v = (y + t - z + x) / 2;
		dragon(order - 1, x, y, u, v);
		dragonInverse(order - 1, u, v, z, t);
	}
	/* END HIDDEN */
}

void dragonInverse(int order, double x, double y, double z, double t) {
	/* BEGIN HIDDEN */
	double u, v;

	if (order == 1) {
		setColor(blue);
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

void run() {
	dragon(getParamInt(0),  getParamDouble(1),  getParamDouble(2), getParamDouble(3), getParamDouble(4));
}
