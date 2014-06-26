#include "RemoteBuggle.h"

void hunt(Color c);
int isFacingTrail(Color c);

int main(){
	hunt(green);
	return 0;
}

void hunt(Color c) {
	while (! isOverBaggle()) {
		if (isFacingTrail(c)) {
			brushDown();
			forward(1);
			brushUp();
		} else {
			left();
		}
	}
	pickupBaggle();
}



#line 1 "SlugTracking"
/* BEGIN TEMPLATE */
int isFacingTrail(Color c){

	/* BEGIN SOLUTION */
	if (isFacingWall())
		return 0;

	forward(1);
	int res = (getGroundColor() == c);
	backward(1);
	return res;

/* END SOLUTION */
}

/* END TEMPLATE */
