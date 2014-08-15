//RemoteBuggle

int isGroundWhite() {
	return getGroundColor()==white;
}

void run(){
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	do {
		forward(1);
	} while (!isGroundWhite());
	/* END SOLUTION */
	/* END TEMPLATE */
}

