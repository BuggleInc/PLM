package lessons.welcome.traversal.diagonal;

import plm.universe.bugglequest.SimpleBuggle;

class TraversalDiagonalEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	var diag = 0;
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var cpt = 0;
		writeMessage(Integer.toString(cpt));
		while (!endingPosition()) {
			nextStep();
			cpt+=1;
			writeMessage(Integer.toString(cpt));
		}
	}

	def nextStep(): Unit = {
		var x = getX();
		var y = getY();

		if ((x + 1 < getWorldWidth()) && (y > 0)) {
			x+=1;
			y-=1;
		} else if (diag + 1 < getWorldHeight()) {
			diag+=1;
			y = diag;
			x = 0;
		} else {
			diag+=1;
			x = diag - (getWorldWidth() - 1);
			y = diag - x;
		}

		setPos(x, y);
	}

	def endingPosition():Boolean = {
		return (getX() == getWorldWidth() - 1) && (getY() == getWorldHeight() - 1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */

}
