#include "RemoteBuggle.h"


void goAndGet();

int main(){

	int i=0;
	for (i=0; i<7; i++) {
		goAndGet();
		right();
		forward(1);
		left();
	}

	return 0;
}

#line 1 "MethodsEntity"
/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
/* END SOLUTION */
/* END TEMPLATE */
