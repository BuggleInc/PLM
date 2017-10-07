//RemoteBuggle

void bigSquare();
void squareB(Color c);
void squareA(Color c);
void mark() ;


/* BEGIN TEMPLATE */

void run(){
	/* BEGIN SOLUTION */
	bigSquare();
	forward(4);
	bigSquare();

	backward(4);
	left();
	forward(4);
	right();

	bigSquare();
	forward(4);
	bigSquare();

}
void mark() {
	brushDown();
	brushUp();
}

void squareA(Color c) {
	setBrushColor(c);

	forward(1);
	mark();

	left();
	forward(1);

	left();
	forward(1);
	mark();

	left();
	forward(1);
	left();
}

void squareB(Color c) {
	setBrushColor(c);
	mark();

	forward(1);

	left();
	forward(1);
	mark();

	left();
	forward(1);

	left();
	forward(1);
	left();
}

void bigSquare() {
	squareA(red);
	forward(2);
	squareB(blue);
	backward(2);
	left();
	forward(2);
	right();
	squareB(yellow);
	forward(2);
	squareA(green);

	backward(2);
	left();
	backward(2);
	right();
/* END SOLUTION */
}

/* END TEMPLATE */
