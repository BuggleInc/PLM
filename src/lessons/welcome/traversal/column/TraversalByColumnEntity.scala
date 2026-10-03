package lessons.welcome.traversal.column;

import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.Game

class TraversalByColumnEntity extends SimpleBuggle {
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

	override def forward(i:Int): Unit = {
		throw new RuntimeException("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead.");
	}
	override def stepForward(): Unit = {
		throw new RuntimeException("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead.");
	}
	override def backward(i:Int): Unit = {
		throw new RuntimeException("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead.");
	}
	override def stepBackward(): Unit = {
		throw new RuntimeException("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead.");
	}
	/* END REMOTE */
}
