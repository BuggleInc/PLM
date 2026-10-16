package lessons.welcome.methods.slug;

import java.awt.Color;


class SlugSnailEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		hunt(getColorIntParam()); 
	}

	/* BEGIN TEMPLATE */
	def hunt(c:Color): Unit = {
		// Write your code here
		/* BEGIN SOLUTION */
		while (! isOverBaggle()) {
			if (isFacingTrail(c)) {
				brushDown();
				step();
				brushUp();
			} else {
				left();
			}
		}
		pickupBaggle();
		/* END SOLUTION */
	}
   
	// here comes your isFacingTrail method   

	/* BEGIN SOLUTION */
	def isFacingTrail(c:Color):Boolean = {
		if (isFacingWall())
			return false;

		step();
		val res = (getGroundColor() == c);
		stepBack();
		return res;
	}		
	/* END SOLUTION */
	/* END TEMPLATE */
	/* END REMOTE */


}
