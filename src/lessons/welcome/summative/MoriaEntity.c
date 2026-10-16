#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

/* BEGIN TEMPLATE */
void run(){
	/* BEGIN SOLUTION */
	back();
	while (!isFacingWall()) {
		while (!isOverBaggle() && !isFacingWall())
                  step();
                if (isOverBaggle()) {
                  pickupBaggle();
                  back();
                  while (!isOverBaggle())
                    step();
                  stepBack();
                  dropBaggle();
                  back();
                  step();
                }
        }
        right();
        step();
        left();
        step();
        /* END SOLUTION */
}

/* END TEMPLATE */
