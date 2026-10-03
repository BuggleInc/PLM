package lessons.welcome.traversal.zigzag;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

class TraversalZigZagEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */		
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var cpt=0;
		writeMessage(Integer.toString(cpt));
		while (!endingPosition()) {
			nextStep();
			cpt+=1;
			writeMessage(Integer.toString(cpt));
		}
	}

	def nextStep(): Unit = {        
		var x=getX();
		var y=getY();

		if (y % 2 == 0) {
			if (x < getWorldWidth()-1) {
				x+=1;
			} else if (y < getWorldHeight()-1) {
				y+=1; 
			}
		} else {
			if (0 < x) {
				x-=1;
			} else if (y < getWorldHeight()-1) {
				y+=1; 
			}
		}

		setPos(x,y);
	}

	def endingPosition():Boolean = {
		return (getX() == getWorldWidth() -1) && (getY() == getWorldHeight()-1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */	
	/* END REMOTE */


	override def forward(i:Int): Unit = {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead."));
	}
	override def stepForward(): Unit = {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead."));
	}
	override def backward(i:Int): Unit = {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead."));
	}
	override def stepBackward(): Unit = {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead."));
	}
}
