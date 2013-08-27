package lessons.turmites.langton;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;

public class LangtonEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	public void step() {
		/* BEGIN SOLUTION */
		if (getGroundColor().equals(Color.white)) {
			right();

			setBrushColor(Color.black);
			brushDown();
			brushUp();

			forward();
		} else {
			left();

			setBrushColor(Color.white);
			brushDown();
			brushUp();

			forward();				
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	@Override
	public void run() { 
		int nbSteps = (Integer)getParam(0); 
		for (int i=0;i<nbSteps;i++) {
			step();
			((lessons.turmites.universe.TurmiteWorld)world).stepDone();
		}
	}
}
