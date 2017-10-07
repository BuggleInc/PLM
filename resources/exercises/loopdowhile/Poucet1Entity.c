//RemoteBuggle

int crossing() {
	return getX()%5== 1 && getY()%5==1;
}
int exitReached() {
	return getGroundColor()==orange;
}

void run(){
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	while (!exitReached()) {
		int seen = 0;

		do {
			forward(1);
			if (isOverBaggle())
				seen++;
		} while (! crossing());

		if (seen>2)
			left();
		else
			right();
	}
	forward(1);
	/* END SOLUTION */
	/* END TEMPLATE */
}
