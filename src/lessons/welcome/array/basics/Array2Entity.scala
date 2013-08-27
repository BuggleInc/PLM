package lessons.welcome.array.basics;

import java.awt.Color
import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.Game

class ScalaArray2Entity extends SimpleBuggle {
	override def setX(i: Int)  {
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setX() in this exercise."));
	}
	override def setY(i: Int)  { 
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setY() in this exercise."));
	}
	override def setPos(x: Int, y:Int)  { 
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setPos() in this exercise."));
	}

	/* BEGIN TEMPLATE */
	def run() {
	/* BEGIN SOLUTION */
		val colors = new Array[Color](getWorldHeight());

		/* read the colors */
		colors(0) = getGroundColor();
		for (i <- 1 to getWorldHeight()-1) {
			forward();
			colors(i) = getGroundColor();
		}
		backward(getWorldHeight()-1);

		/* duplicate the pattern */
		for (col <- 1 to getWorldWidth()-1) {
			left();
			forward();
			right();
			makeLine(colors);
		}
	}

	def makeLine(colors: Array[Color]) {
		val offset = readMessage().toInt;
		mark(colors( (0+offset)%colors.length ) );
		for (i <- 1 to getWorldWidth()-1) {
			forward();
			mark(colors(  (i+offset)%colors.length  ));
		}
		backward(getWorldHeight()-1);
	}
	def mark(c:Color){
		setBrushColor(c);
		brushDown();
		brushUp();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
