package lessons.welcome.loopfor;

import java.awt.Color
import plm.universe.bugglequest.SimpleBuggle

class LoopCourseEntity extends SimpleBuggle {

	var colors = Array(
			Color.white,
			new Color(255,240,240),new Color(255,220,220),new Color(255,205,205),
			new Color(255,190,190),new Color(255,170,170),new Color(255,150,150),
			new Color(255,130,130),new Color(255,110,110),new Color(255,45,45),
			new Color(255,5,5), Color.magenta)

	override def stepForward(): Unit = {
		super.stepForward();
		var c = getGroundColor();
		var nextColor:Color = null;
		for (i <- 0 to colors.length-1)
			if (colors(i).equals(c)) { 
				if (i==colors.length-1)
					nextColor = colors(i)
				else	
					nextColor = colors(i+1);
			}
		setBrushColor(nextColor);
		brushDown();
		brushUp();
	}

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		for (i <- 1 to 10; side <- 1 to 4) {
			for (step <- 1 to 8) {
				stepForward()
			}
			left()
		} 
		/* END SOLUTION */
	}
	/* END REMOTE */
}
