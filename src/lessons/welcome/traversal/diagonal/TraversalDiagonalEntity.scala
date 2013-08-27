package lessons.welcome.traversal.diagonal;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

class ScalaTraversalDiagonalEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	var diag = 0;

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
	}

	def run() {
		var cpt = 0;
		writeMessage(cpt);
		while (!endingPosition()) {
			nextStep();
			cpt+=1;
			writeMessage(cpt);
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */


	override def forward(i:Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward() in this exercise."));
	}
	override def forward()  {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward() in this exercise."));
	}
	override def backward(i:Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward() in this exercise."));
	}
	override def backward() {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward() in this exercise."));
	}
	override def left() {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use left() in this exercise."));
	}
	override def right() {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use right() in this exercise."));
	}
	override def back() {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use back() in this exercise."));
	}
	override def isFacingWall():Boolean = {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use isFacingWall() in this exercise."));
	}
	override def isBackingWall():Boolean = {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use isBackingWall() in this exercise."));
	}
}
