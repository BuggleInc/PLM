package lessons.welcome.loopwhile;

import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.Game

class ScalaLoopWhileEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument"));
	}

	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument"));
	}

	override def run() { 
		/* BEGIN SOLUTION */
		while (!isFacingWall())
			forward();
		/* END SOLUTION */
	}
}
