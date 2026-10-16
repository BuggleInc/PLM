#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run(){
	/* BEGIN SOLUTION */
	int cpt = 0;
	while (cpt != 4) {
          step();
          if (isOverBaggle())
            cpt++;
        }
        /* END SOLUTION */
}
