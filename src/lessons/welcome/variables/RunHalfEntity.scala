package lessons.welcome.variables;

import java.awt.Color;
import jlm.core.model.Game

class ScalaRunHalfEntity extends jlm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
	}
	def isOverOrange():Boolean = {
		return getGroundColor().equals(Color.orange);
	}
	/* BINDINGS TRANSLATION */
	def estSurOrange():Boolean = { return isOverOrange(); }

	override def run() {
		/* BEGIN SOLUTION */
		var baggle:Int = 0;
		var orange:Int = 0;
		while (2 * baggle != orange + 1) {
			//if (getName().equals("buggle2")) 
			//	System.out.println("baggle: "+baggle+"; orange: "+orange+"; sum:"+(2*baggle-orange-1));
			forward();
			if (isOverBaggle())
				baggle += 1
			if (isOverOrange())
				orange += 1
		}
		/* END SOLUTION */
	}
}
