package lessons.welcome.methods.slug;

import java.awt.Color;

class ScalaSlugHuntingEntity extends jlm.universe.bugglequest.SimpleBuggle {

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
				forward();
				brushUp();
			} else {
				left();
			}
		}
		pickupBaggle();
		/* END SOLUTION */
	}
	
	// Copy your isFacingTrail() here
	/* END TEMPLATE */

	/* BEGIN HIDDEN */
	def isFacingTrail():Boolean = {
		if (isFacingWall())
			return false;

		forward();
		var res = getGroundColor() == Color.green;
		backward();
		return res;

	}		
	/* END HIDDEN */

}
