#include "RemoteTurtle.h"

void run();

int main(){
	run();
	return 0;
}


#line 1 "Polygon7.c"
/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int i;
	for (i=0; i<7;i++) {
		forward(80);
		right(360./7.);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
