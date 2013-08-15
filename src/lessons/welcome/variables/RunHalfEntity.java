package lessons.welcome.variables;

import java.awt.Color;

import jlm.core.model.Game;

public class RunHalfEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
	}
	public boolean isOverOrange() {
		return getGroundColor().equals(Color.orange);
	}
	/* BINDINGS TRANSLATION */
	public boolean estSurOrange() { return isOverOrange(); }
	
	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		int baggle = 0;
		int orange = 0;
		while (2 * baggle != orange + 1) {
			//if (getName().equals("buggle2")) 
			//	System.out.println("baggle: "+baggle+"; orange: "+orange+"; sum:"+(2*baggle-orange-1));
			forward();
			if (isOverBaggle())
				baggle++;
			if (isOverOrange())
				orange++;
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
