package lessons.welcome.cells;

import java.awt.Color;

public class LangtonEntity extends jlm.universe.bugglequest.SimpleBuggle {
	void setGroundColor(Color c) {
		setBrushColor(c);
		brushDown();
		brushUp();
	}
	
	/* BEGIN TEMPLATE */
public void step() {
		/* BEGIN SOLUTION */
		if (getGroundColor().equals(Color.white)) {
			turnRight();
			setGroundColor(Color.black);
			forward();
		} else {
			turnLeft();
			setGroundColor(Color.white);
			forward();				
		}
		/* END SOLUTION */
}
	/* END TEMPLATE */
	
	@Override
	public void run() { 
		int nbSteps = (Integer)getParam(0); 
		for (int i=0;i<nbSteps;i++) 
			step();
	}
}
