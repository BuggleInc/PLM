//RemoteTurtle

/* BEGIN TEMPLATE */
void sierpinski(int level, double length) {
	/* BEGIN SOLUTION */
	if (level >= 0) {
		int i;
		for (i = 0; i < 3; i++) {
			 sierpinski(level-1,length/2);
			 forward(length);
			 right(120);
		}
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

void run() {
	sierpinski( getParamInt(0), getParamDouble(1));
}
