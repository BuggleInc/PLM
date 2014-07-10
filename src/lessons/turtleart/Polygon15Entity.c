#include "RemoteTurtle.h"


#line 1 "Polygon15.c"
/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int sides=20;
	int i;
	for (i=0; i<sides;i++) {
		forward(30);
		right(360/sides);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
