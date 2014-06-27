#include "RemoteBuggle.h"


#line 1 "WhileMoria"
/* BEGIN TEMPLATE */

int main(){
	/* BEGIN SOLUTION */
	back();
	while (!isFacingWall()) {
		while (!isOverBaggle() && !isFacingWall())
			forward(1);
		if (isOverBaggle()) {
			pickupBaggle();
			back();
			while (!isOverBaggle())
				forward(1);
			backward(1);
			dropBaggle();
			back();
			forward(1);
		}
	}
	right();
	forward(1);
	left();
	forward(1);
	/* END SOLUTION */
	return 0;
}

/* END TEMPLATE */
