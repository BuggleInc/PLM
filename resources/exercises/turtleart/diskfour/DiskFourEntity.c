//RemoteTurtle

void quadrant();

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	quadrant();
	setColor(red);
	quadrant();
	setColor(orange);
	quadrant();
	setColor(magenta);
	quadrant();
}
void quadrant() {
	int i;
	for (i=0; i<90;i++) {
		forward(100);
		backward(100);
		right(1);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
