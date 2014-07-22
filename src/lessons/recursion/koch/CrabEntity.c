//RemoteTurtle
#include <math.h>
/* BEGIN TEMPLATE */
void crab(int levels, double length) {
	/* BEGIN SOLUTION */
	if (levels == 0) {
		forward(length);
	} else {
		left(45);
		crab(levels-1, length/sqrt(2));
		right(90);
		crab(levels-1, length/sqrt(2));
		left(45);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

void run() {
	crab(getParamInt(0),getParamDouble(1));
}
