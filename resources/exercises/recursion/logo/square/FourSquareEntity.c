//RemoteTurtle

void square();

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int i;
	for (i = 0; i < 4; i++) {
		square();
		right(90);
	}
}
void square() {
	int i;
	for (i = 0; i < 4; i++) {
		forward(100);
		right(90);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
