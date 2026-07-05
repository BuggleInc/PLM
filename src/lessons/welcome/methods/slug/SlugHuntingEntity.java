package lessons.welcome.methods.slug;

import java.awt.Color;


public class SlugHuntingEntity extends plm.universe.bugglequest.SimpleBuggle {

	@Override
	public void run() {
		hunt(); 
	}

	/* BEGIN TEMPLATE */
	public void hunt() {
		// Write your code here
		/* BEGIN SOLUTION */
		while (! isOverBaggle()) {
			if (isFacingTrail()) {
				brushDown();
				stepForward();
				brushUp();
			} else {
				left();
			}
		}
		pickupBaggle();
	}

	boolean isFacingTrail() {
		if (isFacingWall())
			return false;

		stepForward();
		boolean res = getGroundColor()== Color.green;
		stepBackward();
		return res;

		/* END SOLUTION */
	}		
	// Copy your isFacingTrail here
	/* END TEMPLATE */


}
