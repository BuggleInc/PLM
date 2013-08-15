package lessons.welcome.methods.slug;

import java.awt.Color;


class ScalaSlugTrackingEntity extends jlm.universe.bugglequest.SimpleBuggle {

	override def run() {
		hunt(); 
	}

	def hunt() {
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
	}

	/* BEGIN TEMPLATE */
	def isFacingTrail():Boolean = {
		// Write your code here
		/* BEGIN SOLUTION */
		if (isFacingWall())
			return false;
		forward();
		val res = (getGroundColor() == Color.green); 
		backward();
		return res;
		/* END SOLUTION */
	}		
	/* END TEMPLATE */

}
