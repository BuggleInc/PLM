//RemoteBuggle

void stepHandOnWall();

/* BEGIN TEMPLATE */
void run() {

	/* BEGIN SOLUTION */
	// Make sure we have a wall to the left
	left();
	while (!isFacingWall())
		forward(1);
	right();

	while (!isOverBaggle())
		stepHandOnWall();

	pickupBaggle();
}

void stepHandOnWall() {
	// PRE: we have a wall on the left
	// POST: we still have the same wall on the left, are one step ahead

	while (!isFacingWall()) {
		forward(1);
		left(); // change to right to get a right follower
	}
	right(); // change to left to get a right follower

	/* END SOLUTION */
}
/* END TEMPLATE */
