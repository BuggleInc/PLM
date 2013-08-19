package lessons.welcome.array.basics;

import java.awt.Color;
import jlm.core.model.Game

class ScalaArray1Entity extends jlm.universe.bugglequest.SimpleBuggle {
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
		for (i <- 0 to getWorldHeight()-1) {
			colors(i) = getGroundColor()
					forward();
		}

		/* duplicate the pattern */
		for (col <- 1 to getWorldWidth()-1) {
			left();
			forward();
			right();
			forward();
			makeLine(colors);
		}
	}

	def makeLine(colors:Array[Color]) {
		for (i <- 0 to getWorldWidth()-1) {
			mark(colors(i));
			forward();
		}
	}
	
	def mark(c:Color){
		setBrushColor(c);
		brushDown();
		brushUp();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
