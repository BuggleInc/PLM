package lessons.welcome.methods.slug;

import java.awt.Color;

class SlugHuntingEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		hunt(); 
	}

	/* BEGIN TEMPLATE */
	def hunt(): Unit = {
		// Write your code here
		/* BEGIN SOLUTION */
		while (! isOverBaggle()) {
			if (isFacingTrail()) {
				brushDown();
				step();
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

		step();
		var res = getGroundColor() == Color.green;
		stepBack();
		return res;

		/* END SOLUTION */
	}
	
	// Copy your isFacingTrail() here
	/* END TEMPLATE */
	/* END REMOTE */
}
