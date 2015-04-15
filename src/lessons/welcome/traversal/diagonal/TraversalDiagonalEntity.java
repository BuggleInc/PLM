package lessons.welcome.traversal.diagonal;

import plm.universe.bugglequest.SimpleBuggle;

public class TraversalDiagonalEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	int diag = 0;
	public void run() {
		/* BEGIN SOLUTION */
		int cpt = 0;
		writeMessage(cpt);
		while (!endingPosition()) {
			nextStep();
			cpt++;
			writeMessage(cpt);
		}
	}

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
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	@Override
	public void forward(int i)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead."));
	}
	@Override
	public void forward()  {
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead."));
	}
	@Override
	public void backward() {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead."));
	}
}
