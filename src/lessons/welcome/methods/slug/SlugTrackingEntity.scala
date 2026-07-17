package lessons.welcome.methods.slug;

import java.awt.Color;

class SlugTrackingEntity extends plm.universe.bugglequest.SimpleBuggle {

	override def run() {
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

	/* BEGIN TEMPLATE */
	def isFacingTrail():Boolean = {
		// Write your code here
		/* BEGIN SOLUTION */
		if (isFacingWall())
			return false;
		stepForward();
		val res = (getGroundColor() == Color.green); 
		stepBackward();
		return res;
		/* END SOLUTION */
	}		
	/* END TEMPLATE */

}
