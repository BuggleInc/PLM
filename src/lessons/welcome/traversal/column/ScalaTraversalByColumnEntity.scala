package lessons.welcome.traversal.column;

import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.Game

class ScalaTraversalByColumnEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */	
		var cpt=0;
		writeMessage(cpt);
		while (!endingPosition()) {
			nextStep();
			cpt+=1;
			writeMessage(cpt);
		}
	}
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
