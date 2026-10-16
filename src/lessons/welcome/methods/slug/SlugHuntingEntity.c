#include "../../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void hunt();
int isFacingTrail();

void run(){
	hunt();
}


/* BEGIN TEMPLATE */
void hunt() {
/* BEGIN SOLUTION */
	while (! isOverBaggle()) {
		if (isFacingTrail()) {
			brushDown();
                        step();
                        brushUp();
                } else {
                  left();
                }
        }
        pickupBaggle();
}

int isFacingTrail(){
	if (isFacingWall())
		return 0;

        step();
        int res = getGroundColor() == green;
        stepBack();
        return res;

        /* END SOLUTION */
}

/* END TEMPLATE */

