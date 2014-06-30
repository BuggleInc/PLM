#include "RemoteTurtle.h"

void run();

int main(){
	run();
	return 0;
}


#line 1 "CircleTwo.c"
/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int i;
	for (i=0; i<360;i++) {
		forward(1.);
		right(1);
	}
	for (i=0; i<360;i++) {
		forward(2);
		right(1);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
