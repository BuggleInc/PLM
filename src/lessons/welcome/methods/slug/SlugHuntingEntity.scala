package lessons.welcome.methods.slug;

import java.awt.Color;

class SlugHuntingEntity extends plm.universe.bugglequest.SimpleBuggle {

	override def run() {
		hunt(); 
	}

	/* BEGIN TEMPLATE */
	def hunt() {
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
	
	def isFacingTrail():Boolean = {
		if (isFacingWall())
			return false;

		stepForward();
		var res = getGroundColor() == Color.green;
		stepBackward();
		return res;

		/* END SOLUTION */
	}
	
	// Copy your isFacingTrail() here
	/* END TEMPLATE */
}
