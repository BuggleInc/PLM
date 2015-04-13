package lessons.welcome.variables;

import java.awt.Color;

public class RunHalfEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
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
			//	getGame().getLogger().log("baggle: "+baggle+"; orange: "+orange+"; sum:"+(2*baggle-orange-1));
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
