//RemoteBuggle


int isOverOrange() {
	return getGroundColor()==orange;
}

void run(){
	/* BEGIN SOLUTION */
	int baggle = 0;
	int orange = 0;
	while (2 * baggle != orange + 1) {
		forward(1);
		if (isOverBaggle())
			baggle++;
		if (isOverOrange())
			orange++;
	}
	/* END SOLUTION */
}



