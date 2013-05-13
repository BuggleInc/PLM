package lessons.welcome;

import java.awt.Color;


public class SlugHuntingEntity extends jlm.universe.bugglequest.SimpleBuggle {

	@Override
	public void run() {
		hunt(); 
	}

	/* BEGIN TEMPLATE */
	public void hunt() {
		// à completer
	/* BEGIN SOLUTION */
		while (! isOverBaggle()) {
			if (isFacingTrail(Color.green)) {
				brushDown();
				forward();
				brushUp();
			} else {
				turnLeft();
			}
		}
		pickUpBaggle();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
	boolean isFacingTrail(Color trailColor) {
		if (isFacingWall())
			return false;
		
		forward();
		boolean res = getGroundColor() == trailColor; // equals would be better
		backward();
		return res;
	
	}		
	
	
}
