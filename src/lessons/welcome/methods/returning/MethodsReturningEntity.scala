package lessons.welcome.methods.returning;

import com.sun.org.apache.xpath.internal.operations.Bool
import jlm.core.model.Game


class ScalaMethodsReturningEntity extends jlm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}

	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
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
