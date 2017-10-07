//RemoteBuggle

void run(){
	/* BEGIN SOLUTION */
	int cpt = 0;
	while (cpt != 4) {
		forward(1);
		if (isOverBaggle())
			cpt++;
	}
	/* END SOLUTION */
}
