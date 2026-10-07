package lessons.welcome.traversal.line;

import plm.universe.bugglequest.SimpleBuggle;

class TraversalByLineEntity extends SimpleBuggle {
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
	/* END REMOTE */


}
