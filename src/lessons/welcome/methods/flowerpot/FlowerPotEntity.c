//RemoteBuggle

void growFlowers();
void makeFlower(Color c);
void line(Color c1, Color c2) ;
void halfLine(Color c);


void run(){
	growFlowers();
}

/* BEGIN SOLUTION */
void makeFlower(Color c) {
	setBrushColor(c);
	brushDown();
	forward(2);
	backward(1);
	left();
	forward(1);
	backward(2);
	forward(1);
	setBrushColor(yellow);
	brushUp();
	right();
}

void line(Color c1, Color c2) {
	makeFlower(c1);
	forward(3);
	makeFlower(c2);
	backward(5);
}
void halfLine(Color c) {
	forward(2);
	makeFlower(c);
	backward(3);
}
void growFlowers() {
	line(red, cyan);

	right();
	forward(2);
	left();
	halfLine(orange);
	right();
	forward(2);
	left();

	line(pink, green);
}

/* END SOLUTION */
