package lessons.welcome.methods.returning;

import com.sun.org.apache.xpath.internal.operations.Bool
import plm.core.model.Game


class ScalaMethodsReturningEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def run() { 
		for (i <- 1 to 7) {
			if (haveBaggle()) 
				return;
			right();
			forward();
			left();
		}
	}
	/* BEGIN TEMPLATE */
	def haveBaggle():Boolean = {
		/* BEGIN SOLUTION */
		var res = false
		for (i <- 1 to 6) {
			if (isOverBaggle()) 
				res = true;
			forward();
		}
		backward(6);
		return res;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
