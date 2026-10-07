package lessons.welcome.variables;

import java.awt.Color;

class RunHalfEntity extends plm.universe.bugglequest.SimpleBuggle {
	/* BINDINGS TRANSLATION */
	def estSurOrange():Boolean = { return isOverOrange(); }

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		var baggle:Int = 0;
		var orange:Int = 0;
		while (2 * baggle != orange + 1) {
			//if (getName().equals("buggle2")) 
			//	System.out.println("baggle: "+baggle+"; orange: "+orange+"; sum:"+(2*baggle-orange-1));
			stepForward();
			if (isOverBaggle())
				baggle += 1
			if (isOverOrange())
				orange += 1
		}
		/* END SOLUTION */
		/* END TEMPLATE */
	}
	/* END REMOTE */
}
