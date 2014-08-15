//RemoteBuggle

void run();
int endingPosition();
void nextStep();


/* BEGIN TEMPLATE */
int diag = 0;
void run() {
	/* BEGIN SOLUTION */
	int cpt=0;
	do {
		writeMessage(int2str(cpt));
		nextStep();
		cpt++;
	} while (!endingPosition());
	writeMessage(int2str(cpt));
}

void nextStep() {
	int x = getX();
	int y = getY();

	if ((x + 1 < getWorldWidth()) && (y > 0)) {
		x++;
		y--;
	} else if (diag + 1 < getWorldHeight()) {
		diag++;
		y = diag;
		x = 0;
	} else {
		diag++;
		x = diag - (getWorldWidth() - 1);
		y = diag - x;
	}

	setPos(x, y);
}

int endingPosition() {
	return (getX() == getWorldWidth()-1) && (getY() == getWorldHeight()-1);
	/* END SOLUTION */
}
/* END TEMPLATE */
