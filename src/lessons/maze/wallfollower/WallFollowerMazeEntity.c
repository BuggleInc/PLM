#include "RemoteBuggle.h"


void stepHandOnWall();
void run();

int main(){
	run();
	return 0;
}

#line 1 "WallFollowerMaze"
/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void stepHandOnWall() {
	// PRE: we have a wall on the left
	// POST: we still have the same wall on the left, are one step ahead

	while (!isFacingWall()) {
		forward(1);
		left(); // change to right to get a right follower
	}
	right(); // change to left to get a right follower
}

void run() {
	// Make sure we have a wall to the left
	left();
	while (!isFacingWall())
		forward(1);
	right();

	while (!isOverBaggle())
		stepHandOnWall();
	pickupBaggle();
}
/* END SOLUTION */
/* END TEMPLATE */
