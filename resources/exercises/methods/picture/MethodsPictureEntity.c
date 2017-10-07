//RemoteBuggle

void mark();
void makeV(Color c);
void makePattern();
void nextLine();
void makeLine(int count);


/* BEGIN TEMPLATE */

void run(){
	/* BEGIN SOLUTION */
	int i;
	for (i=0; i<3;i++) {
			makeLine(3);
			nextLine();
	}
}



void mark() {
	brushDown();
	brushUp();
}

void makeV(Color c) {
	setBrushColor(c);
	forward(1);
	mark();

	forward(1);
	left();
	forward(1);
	mark();

	backward(1);
	right();
	forward(1);
	mark();

	forward(1);
	left();
}

void makePattern() {
	makeV(yellow);
	makeV(red);
	makeV(blue);
	makeV(green);
	forward(5);
}

void makeLine(int count){
	int i;
	for (i=0; i<count;i++)
		makePattern();
	backward(count*5);
}

void nextLine() {
	left();
	forward(5);
	right();

	/* END SOLUTION */
}
/* END TEMPLATE */
