package lessons.welcome.slug.tracking;

import java.awt.Color;


public class SlugTrackingEntity extends jlm.universe.bugglequest.SimpleBuggle {

	@Override
	public void run() {
		hunt(); 
	}

	public void hunt() {
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
	}

	/* BEGIN TEMPLATE */
	boolean isFacingTrail(Color trailColor) {
		// Write your code here
		/* BEGIN SOLUTION */
		if (isFacingWall())
			return false;
		forward();
		boolean res = getGroundColor().equals(trailColor); 
		backward();
		return res;
		/* END SOLUTION */
	}		
	/* END TEMPLATE */

}
