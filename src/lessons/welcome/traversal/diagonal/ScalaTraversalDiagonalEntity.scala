package lessons.welcome.traversal.diagonal;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

class ScalaTraversalDiagonalEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	var diag = 0;
	override def run() {
		/* BEGIN SOLUTION */
		var cpt = 0;
		writeMessage(cpt);
		while (!endingPosition()) {
			nextStep();
			cpt+=1;
			writeMessage(cpt);
		}
	}

	def nextStep() {
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

	override def forward(i:Int)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead."));
	}
	override def forward()  {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead."));
	}
	override def backward(i:Int) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead."));
	}
	override def backward() {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead."));
	}
}
