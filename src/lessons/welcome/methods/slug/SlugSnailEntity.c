#include "RemoteBuggle.h"

void hunt(Color c);
int isFacingTrail(Color c);

int main(){
	hunt(getParam());
	return 0;
}

#line 1 "SlugSnail"
/* BEGIN TEMPLATE */
void hunt(Color c) {
	/* BEGIN SOLUTION */
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


int isFacingTrail(Color c){
	if (isFacingWall())
		return 0;

	forward(1);
	int res = (getGroundColor() == c);
	backward(1);
	return res;

	/* END SOLUTION */
}

/* END TEMPLATE */
