//RemoteBuggle

void run();
int endingPosition();
void nextStep();

/* BEGIN TEMPLATE */
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
	int x=getX();
	int y=getY();

	if (y % 2 == 0) {
		if (x < getWorldWidth()-1) {
			x++;
		} else if (y < getWorldHeight()-1) {
			y++;
		}
	} else {
		if (0 < x) {
			x--;
		} else if (y < getWorldHeight()-1) {
			y++;
		}
	}

	setPos(x,y);
}

int endingPosition() {
	return (getX() == getWorldWidth()-1) && (getY() == getWorldHeight()-1);
	/* END SOLUTION */
}
/* END TEMPLATE */
