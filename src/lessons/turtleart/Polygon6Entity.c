#include "RemoteTurtle.h"

void run();


int main(){
	run();
	return 0;
}


#line 1 "Polygon6.c"
/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int i;
	for (i=0; i<6;i++) {
		forward(80);
		right(360/6);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

