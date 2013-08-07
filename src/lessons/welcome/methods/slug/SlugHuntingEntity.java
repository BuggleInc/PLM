package lessons.welcome.methods.slug;

import java.awt.Color;


public class SlugHuntingEntity extends jlm.universe.bugglequest.SimpleBuggle {

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
				forward();
				brushUp();
			} else {
				left();
			}
		}
		pickupBaggle();
		/* END SOLUTION */
	}

	/* BEGIN HIDDEN */
	boolean isFacingTrail() {
		if (isFacingWall())
			return false;

		forward();
		boolean res = getGroundColor().equals(Color.green);
		backward();
		return res;

	}		
	/* END HIDDEN */
	/* END TEMPLATE */


}
