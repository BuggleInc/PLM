package lessons.welcome.methods.slug;

import java.awt.Color;

class SlugTrackingEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
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

	/* BEGIN TEMPLATE */
	def isFacingTrail():Boolean = {
		// Write your code here
		/* BEGIN SOLUTION */
		if (isFacingWall())
			return false;
		step();
		val res = (getGroundColor() == Color.green); 
		stepBack();
		return res;
		/* END SOLUTION */
	}		
	/* END TEMPLATE */
	/* END REMOTE */

}
