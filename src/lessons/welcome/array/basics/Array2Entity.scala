package lessons.welcome.array.basics;

import java.awt.Color
import plm.universe.bugglequest.SimpleBuggle;

class Array2Entity extends SimpleBuggle {
	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
	/* BEGIN SOLUTION */
		val colors = new Array[Color](getWorldHeight());

		/* read the colors */
		colors(0) = getGroundColor();
		for (i <- 1 to getWorldHeight()-1) {
			stepForward();
			colors(i) = getGroundColor();
		}
		backward(getWorldHeight()-1);

		/* duplicate the pattern */
		for (col <- 1 to getWorldWidth()-1) {
			left();
			stepForward();
			right();
			makeLine(colors);
		}
	}

	def makeLine(colors: Array[Color]): Unit = {
		val offset = readMessage().toInt;
		mark(colors( (0+offset)%colors.length ) );
		for (i <- 1 to getWorldWidth()-1) {
			stepForward();
			mark(colors(  (i+offset)%colors.length  ));
		}
		backward(getWorldHeight()-1);
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
