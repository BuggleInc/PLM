//RemoteBuggle

void step();


void run(){
	int nbSteps = getParam();
	int i;
	for (i=0;i<nbSteps;i++) {
		step();
		stepDone();
	}
}

/* BEGIN TEMPLATE */
void step() {
	/* BEGIN SOLUTION */
	if (getGroundColor()==white) {
		right();

		setBrushColor(black);
		brushDown();
		brushUp();

		forward(1);
	} else {
		left();

		setBrushColor(white);
		brushDown();
		brushUp();

		forward(1);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
