#include "RemoteBuggle.h"

int main(){
	#line 1 "LoopFor"
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	int cpt = 0;
	while (!isOverBaggle()) {
		cpt++;
		forward(1);
	}
	pickupBaggle();
	int cpt2;
	for (cpt2=0 ; cpt2<cpt ; cpt2++) {
		backward(1);
	}
	dropBaggle();
	/* END SOLUTION */
	/* END TEMPLATE */
	return 0;
}
