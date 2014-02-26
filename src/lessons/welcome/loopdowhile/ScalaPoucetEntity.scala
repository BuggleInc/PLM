package lessons.welcome.loopdowhile;

import java.awt.Color;
import plm.core.model.Game

class ScalaPoucetEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	def crossing(): Boolean = {
		return getX()%5== 1 && getY()%5==1;
	}
	def exitReached(): Boolean = {
		return getGroundColor().equals(Color.orange);
	}
	/* BINDINGS TRANSLATION */
	def sortieTrouvee(): Boolean = { return exitReached() }
	def croisement(): Boolean = { return crossing() }

	override def run() { 
		/* BEGIN SOLUTION */
		while (!exitReached()) {
			var count = 0;
			
			do {
				forward();
				if (isOverBaggle())
					count+=1;
			} while (! crossing());
			
			if (count>2)
				left();
			else
				right();
		}
		forward();
		/* END SOLUTION */
	}
}
