//RemoteBuggle

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
void run(){
	hunt(green);
}
