//RemoteBuggle

void nextStep();
int endingPosition();

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
    int cpt=0;
	writeMessage(int2str(cpt));
	while (!endingPosition()) {
		nextStep();
		cpt++;
		writeMessage(int2str(cpt));
	}
}
void nextStep() {
	int x=getX();
	int y=getY();

	if (y < getWorldHeight()-1) {
		y++;
	} else {
		y = 0;
		if (x < getWorldWidth()-1) {
			x++;
		} else {
			x = 0;
		}
	}
	setPos(x,y);
}

int endingPosition() {
	return (getX() == getWorldWidth() -1) && (getY() == getWorldHeight()-1);
	/* END SOLUTION */
}
/* END TEMPLATE */
