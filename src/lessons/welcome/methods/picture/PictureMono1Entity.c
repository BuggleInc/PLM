//RemoteBuggle

void mark();
void makeV();

/* BEGIN TEMPLATE */
void run(){
	/* BEGIN SOLUTION */
	makeV();
	makeV();
	makeV();
	makeV();
}

void mark() {
	brushDown();
	brushUp();
}

void makeV() {
	forward(2);
	mark();

	forward(1);
	left();
	forward(1);
	mark();

	backward(1);
	right();
	forward(1);
	mark();

	forward(2);
	left();
/* END SOLUTION */

}
/* END TEMPLATE */
