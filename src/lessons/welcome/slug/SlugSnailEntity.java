package lessons.welcome.slug;

import java.awt.Color;


public class SlugSnailEntity extends jlm.universe.bugglequest.SimpleBuggle {

	@Override
	public void run() {
		hunt((Color) getParam(0)); 
	}

	/* BEGIN TEMPLATE */
	public void hunt(Color c) {
		// Write your code here
		/* BEGIN SOLUTION */
		while (! isOverBaggle()) {
			if (isFacingTrail(c)) {
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
   
	// here comes your isFacingTrail method   

	/* BEGIN HIDDEN */
	boolean isFacingTrail(Color c) {
		if (isFacingWall())
			return false;

		forward();
		boolean res = getGroundColor().equals(c);
		backward();
		return res;

	}		
	/* END HIDDEN */
	/* END TEMPLATE */


}
