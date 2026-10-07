package lessons.welcome.array.basics;

import java.awt.Color;

class Array1Entity extends plm.universe.bugglequest.SimpleBuggle {
	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		val colors = new Array[Color](getWorldHeight());

		/* read the colors */
		for (i <- 0 to getWorldHeight()-1) {
			colors(i) = getGroundColor()
					stepForward();
		}

		/* duplicate the pattern */
		for (col <- 1 to getWorldWidth()-1) {
			left();
			stepForward();
			right();
			stepForward();
			makeLine(colors);
		}
	}

	def makeLine(colors:Array[Color]): Unit = {
		for (i <- 0 to getWorldWidth()-1) {
			mark(colors(i));
			stepForward();
		}
	}
	
	def mark(c:Color): Unit = {
		setBrushColor(c);
		brushDown();
		brushUp();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
