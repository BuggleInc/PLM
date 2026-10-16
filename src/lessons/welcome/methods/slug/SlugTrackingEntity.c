#include "../../../../../target/classes/resources/langages/c/RemoteBuggle.h"

/* BEGIN TEMPLATE */
int isFacingTrail(Color c){

	/* BEGIN SOLUTION */
	if (isFacingWall())
		return 0;

        step();
        int res = (getGroundColor() == c);
        stepBack();
        return res;

        /* END SOLUTION */
}
/* END TEMPLATE */


void hunt(Color c) {
	while (! isOverBaggle()) {
		if (isFacingTrail(c)) {
			brushDown();
                        step();
                        brushUp();
                } else {
                  left();
                }
        }
        pickupBaggle();
}
void run(){
	hunt(green);
}
