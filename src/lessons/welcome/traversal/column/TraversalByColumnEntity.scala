package lessons.welcome.traversal.column;

import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.Game

class ScalaTraversalByColumnEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */	
	def nextStep() {	
		var x=getX();
		var y=getY();

		if (y < getWorldHeight()-1) {
			y+=1;
		} else {
			y = 0;
			if (x < getWorldWidth()-1) {
				x+=1;
			} else {
				x = 0; 
			}
		}
		setPos(x,y);
	}

	def endingPosition(): Boolean = {
		return (getX() == getWorldWidth() -1) && (getY() == getWorldHeight()-1);
	}


	def run() {
		var cpt=0;
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
