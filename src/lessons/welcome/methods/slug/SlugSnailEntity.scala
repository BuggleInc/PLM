package lessons.welcome.methods.slug;

import java.awt.Color;


class SlugSnailEntity extends plm.universe.bugglequest.SimpleBuggle {

	override def run() {
		hunt(getParam(0).asInstanceOf[Color]); 
	}

	/* BEGIN TEMPLATE */
	def hunt(c:Color) {
		// Write your code here
		/* BEGIN SOLUTION */
		while (! isOverBaggle()) {
			if (isFacingTrail(c)) {
				brushDown();
				stepForward();
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
	def isFacingTrail(c:Color):Boolean = {
		if (isFacingWall())
			return false;

		stepForward();
		val res = (getGroundColor() == c);
		stepBackward();
		return res;
	}		
	/* END HIDDEN */
	/* END TEMPLATE */


}
