//RemoteTurtle

void house(int len);
void line();
void nextLine();

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */

	line();
	house(30);
	penUp();
	right(90);
	forward(150);
	left(90);
	penDown();
	house(30);
	penUp();
	left(90);
	forward(150);
	right(90);
	forward(75);
	penDown();
	line();
}
void nextLine() {
	 penUp();
	 left(90);
	 forward(200);
	 right(90);
	 forward(75);
	 penDown();
}
void line() {
	int cpt;
	for (cpt=0;cpt<4;cpt++) {
		house(30);
		leveCrayon();
		right(90);
		forward(50);
		left(90);
		penDown();
	 }
	 nextLine();
}

void house(int len) {
	forward(len);
	int i;
	right(30);
	for (i = 0; i < 3; i++) {
		forward(len);
		right(120);
	}

	right(60);
	for (i = 0; i < 3; i++) {
		forward(len);
		right(90);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
