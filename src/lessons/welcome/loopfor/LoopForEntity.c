#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run(){
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	int cpt = 0;
	while (!isOverBaggle()) {
		cpt++;
                step();
        }
        pickupBaggle();
        int cpt2;
        for (cpt2 = 0; cpt2 < cpt; cpt2++) {
          stepBack();
        }
        dropBaggle();
        /* END SOLUTION */
        /* END TEMPLATE */
}
