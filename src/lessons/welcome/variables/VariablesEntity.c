#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run(){
	/* BEGIN SOLUTION */
	int cpt = 0;
	while (!isOverBaggle()) {
		cpt++;
		forward(1);
	}
	pickupBaggle();
	while (cpt>0) {
		backward(1);
		cpt--;
	}
	dropBaggle();
	/* END SOLUTION */
}
