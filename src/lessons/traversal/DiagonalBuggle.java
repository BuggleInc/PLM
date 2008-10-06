package lessons.traversal;

import bugglequest.core.SimpleBuggle;

public class DiagonalBuggle extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	int diag = 0;

	public void nextStep() {
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

	public boolean endingPosition() {
		return (getX() == getWorldWidth() - 1) && (getY() == getWorldHeight() - 1);
	}

	@Override
	public void run() {
		int cpt = 0;
		writeMessage(cpt);
		while (!endingPosition()) {
			nextStep();
			cpt++;
			writeMessage(cpt);
		}
	}

	/* END TEMPLATE */
}
