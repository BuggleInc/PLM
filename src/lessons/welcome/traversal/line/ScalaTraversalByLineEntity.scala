package lessons.welcome.traversal.line;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

class ScalaTraversalByLineEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		var cpt=0;
		do {
			writeMessage(cpt);
			nextStep();
			cpt+=1;
		} while (!endingPosition());
		writeMessage(cpt);
	}

	def nextStep() {
		var x=getX();
		var y=getY();
		if (x < getWorldWidth()-1) {
			x+=1;
		} else {
			x = 0; 
			if (y < getWorldHeight()-1) {
				y+=1; 
			} else {
				y = 0;
			}
		}
		setPos(x,y);
	}

	def endingPosition():Boolean = {
		return (getX() == getWorldWidth()-1) && (getY() == getWorldHeight()-1);
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
