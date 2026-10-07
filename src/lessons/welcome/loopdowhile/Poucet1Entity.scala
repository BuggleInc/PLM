package lessons.welcome.loopdowhile;

import java.awt.Color
import plm.universe.GridWorld
import plm.universe.bugglequest.BuggleWorldCell

class Poucet1Entity extends plm.universe.bugglequest.SimpleBuggle {

	/* BINDINGS TRANSLATION */
	def sortieTrouvee(): Boolean = { return exitReached() }
	def croisement(): Boolean = { return crossing() }

	/* BEGIN REMOTE */
	override def run(): Unit = { 
		/* BEGIN SOLUTION */
		while (!exitReached()) {
			var count = 0;
			
			var within = false;
			while (!within || !crossing()) {
				within = true;
				stepForward();
				if (isOverBaggle())
					count+=1;
			}
			
			if (count>2)
				left();
			else
				right();
		}
		stepForward();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
