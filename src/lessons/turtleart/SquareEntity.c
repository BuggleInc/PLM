#include "RemoteTurtle.h"

void run();

int main(){
	run();
	return 0;
}


#line 1 "Square.c"
/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int i;
	for (i = 0; i < 4; i++) {
		forward(200);
		right(90);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
