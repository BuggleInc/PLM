package lessons.recursion.logo.koch;

import plm.universe.turtles.Turtle;
import java.awt.Color

class ScalaHexaKochEntity extends Turtle {

	/* BEGIN TEMPLATE */
    def drawCurve(levels:Int, length:Double) {
    	hexaKoch(levels, length);
    }
	def hexaKoch(levels:Int, length:Double) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);;
		} else {
			hexaKoch(levels-1, length*.14);
			left(120);
			for (i <- 1 to 5) {
				hexaKoch(levels-1, length*.14);
				right(60);
			}
			left(180);
			hexaKoch(levels-1, length*.14);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	override def run() {
		drawCurve(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Double]);
	}
}
