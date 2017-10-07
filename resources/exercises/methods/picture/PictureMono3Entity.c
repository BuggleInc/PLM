//RemoteBuggle

void mark();
void makeV();
void makePattern();
void makeLine(int count);
void nextLine();


/* BEGIN TEMPLATE */

void run(){
	/* BEGIN SOLUTION */
	int i;
	for (i=0; i<9; i++) {
		makeLine(9);
		nextLine();
	}
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
}

void makePattern() {
	makeV();
	makeV();
	makeV();
	makeV();
	forward(7);
}

void makeLine(int count){
	int i;
	for (i=0; i<count;i++)
		makePattern();
	backward(count*7);
}

void nextLine() {
	left();
	forward(7);
	right();
	/* END SOLUTION */
}
/* END TEMPLATE */
