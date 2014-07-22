//RemoteTurtle


/* BEGIN TEMPLATE */
void polygonFractal (int levels, int sides, double length, double shrink) {
	/* BEGIN SOLUTION */
	if (levels == 0) {
		/* do nothing */
	} else {
		int i;
		for (i=0;i<sides;i++) {
			forward(length);

			left((sides-2)*360/(sides*2));
			polygonFractal(levels-1, sides, length*shrink,shrink);
			right((sides-2)*360/(sides*2));
			right(360/sides);

		}
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

void run() {
	polygonFractal(getParamInt(0),getParamInt(1),getParamDouble(2),getParamDouble(3));
}
