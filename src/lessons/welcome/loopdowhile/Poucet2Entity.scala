package lessons.welcome.loopdowhile;

import java.awt.Color
import plm.core.model.Game
import plm.universe.GridWorld
import plm.universe.bugglequest.BuggleWorldCell

class Poucet2Entity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	/* BINDINGS TRANSLATION */
	def sortieTrouvee(): Boolean = { return exitReached() }
	def croisement(): Boolean = { return crossing() }

	override def run() { 
		/* BEGIN SOLUTION */
		while (!exitReached()) {
			var count = 0;
			
			do {
				stepForward();
				if (isOverBaggle())
					count+=1;
			} while (! crossing());
			
			if (count>2)
				left();
			else
				right();
		}
		stepForward();
		/* END SOLUTION */
	}
}
