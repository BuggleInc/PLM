//RemoteBuggle

int endingPosition();
void snakeStep() ;

/* BEGIN TEMPLATE */
void run() {
/* BEGIN SOLUTION */
	brushDown();
	while (!endingPosition()) {
		snakeStep();
	}
}
int endingPosition() {
	if (! isFacingWall())
		return 0;

	int res = 0;
	left();
	if (isFacingWall())
		res = 1;
	right();
	return res;
}

void snakeStep() {
	if (isFacingWall()) {
		if (getDirection() == EAST) {
			left();
			forward(1);
			left();
		} else {
			right();
			forward(1);
			right();
		}
	} else {
		forward(1);
	}

	/* END SOLUTION */
}
/* END TEMPLATE */
