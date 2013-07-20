package lessons.welcome.variables;

import java.awt.Color;

public class RunHalfEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("forward(int) forbidden in this exercise");
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("backward(int) forbidden in this exercise");
	}
	public boolean isOverOrange() {
		return getGroundColor().equals(Color.orange);
	}

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
