package lessons.welcome.cells;

import java.awt.Color;

public class LangtonEntity extends jlm.universe.bugglequest.SimpleBuggle {
	/* BEGIN TEMPLATE */
public void step() {
		/* BEGIN SOLUTION */
		if (getGroundColor().equals(Color.white)) {
			turnRight();
			
			setBrushColor(Color.black);
			brushDown();
			brushUp();
			
			forward();
		} else {
			turnLeft();
			
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
			((lessons.welcome.cells.TurmiteWorld)world).stepDone();
		}
	}
}
