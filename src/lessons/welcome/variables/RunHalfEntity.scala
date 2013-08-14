package lessons.welcome.variables;

import java.awt.Color;

class ScalaRunHalfEntity extends jlm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException("forward(int) forbidden in this exercise");
	}
	override def backward(i: Int) {
		throw new RuntimeException("backward(int) forbidden in this exercise");
	}
	def isOverOrange():Boolean = {
		return getGroundColor().equals(Color.orange);
	}
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
