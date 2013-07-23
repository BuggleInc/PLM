package lessons.welcome.slug;

import java.awt.Color;


public class SlugTrackingEntity extends jlm.universe.bugglequest.SimpleBuggle {

	@Override
	public void run() {
		hunt(); 
	}

	public void hunt() {
		while (! isOverBaggle()) {
			if (isFacingTrail()) {
				brushDown();
				forward();
				brushUp();
			} else {
				turnLeft();
			}
		}
		pickupBaggle();
	}

	/* BEGIN TEMPLATE */
	boolean isFacingTrail() {
		// Write your code here
		/* BEGIN SOLUTION */
		if (isFacingWall())
			return false;
		forward();
		boolean res = getGroundColor().equals(Color.green); 
		backward();
		return res;
		/* END SOLUTION */
	}		
	/* END TEMPLATE */

}
