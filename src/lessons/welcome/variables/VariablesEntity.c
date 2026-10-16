#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run(){
	/* BEGIN SOLUTION */
	int cpt = 0;
	while (!isOverBaggle()) {
		cpt++;
                step();
        }
        pickupBaggle();
        while (cpt > 0) {
          stepBack();
          cpt--;
        }
        dropBaggle();
        /* END SOLUTION */
}
