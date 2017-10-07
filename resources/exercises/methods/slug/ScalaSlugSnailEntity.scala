package lessons.welcome.methods.slug;

import java.awt.Color;


class ScalaSlugSnailEntity extends plm.universe.bugglequest.SimpleBuggle {

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
	def isFacingTrail(c:Color):Boolean = {
		if (isFacingWall())
			return false;

		forward();
		val res = (getGroundColor() == c);
		backward();
		return res;
	}		
	/* END HIDDEN */
	/* END TEMPLATE */


}
