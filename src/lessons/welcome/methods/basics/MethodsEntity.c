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
void goAndGet() {
	int i = 0;
	while (!isOverBaggle()) {
		i++;
		forward(1);
	}
	pickupBaggle();
	while (i>0) {
		backward(1);
		i--;
	}
	dropBaggle();
}
/* END SOLUTION */
/* END TEMPLATE */
