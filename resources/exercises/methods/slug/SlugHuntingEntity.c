//RemoteBuggle

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
			forward(1);
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

	forward(1);
	int res = getGroundColor() == green;
	backward(1);
	return res;

/* END SOLUTION */
}

/* END TEMPLATE */

