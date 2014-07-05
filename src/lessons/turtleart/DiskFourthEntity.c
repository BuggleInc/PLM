#include "RemoteTurtle.h"

void run();

int main(){
	run();
	return 0;
}


#line 1 "DiskFourth.c"
/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int i;
	for (i=0; i<90;i++) {
		forward(100);
		backward(100);
		right(1);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
