//RemoteTurtle

void quadrant();

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int i;
	for (i=0;i<9;i++) {
		setColor(black);
		quadrant();
		setColor(red);
		quadrant();
	}
}
void quadrant() {
	int i;
	for (i=0; i<20;i++) {
		forward(100);
		backward(100);
		right(1);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
