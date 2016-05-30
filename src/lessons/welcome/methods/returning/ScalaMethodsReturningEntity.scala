package lessons.welcome.methods.returning;

import com.sun.org.apache.xpath.internal.operations.Bool
import plm.core.model.Game


class ScalaMethodsReturningEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}

	override def backward(i: Int) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

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
		for (i <- 1 to 6) 
			backward();
		return res;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
