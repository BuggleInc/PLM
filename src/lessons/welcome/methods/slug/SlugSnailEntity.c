#include "../../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void hunt(Color c);
int isFacingTrail(Color c);

void run(){
  hunt(getColorIntParam());
}

/* BEGIN TEMPLATE */
void hunt(Color c) {
	/* BEGIN SOLUTION */
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


int isFacingTrail(Color c){
	if (isFacingWall())
		return 0;

        step();
        int res = (getGroundColor() == c);
        stepBack();
        return res;

        /* END SOLUTION */
}

/* END TEMPLATE */
