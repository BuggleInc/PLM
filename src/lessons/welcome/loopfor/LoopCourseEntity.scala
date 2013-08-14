package lessons.welcome.loopfor;

import java.awt.Color
import jlm.universe.bugglequest.SimpleBuggle
import jlm.core.model.Game

class ScalaLoopCourseEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
	}

	var colors = Array(
			Color.white,
			new Color(255,240,240),new Color(255,220,220),new Color(255,205,205),
			new Color(255,190,190),new Color(255,170,170),new Color(255,150,150),
			new Color(255,130,130),new Color(255,110,110),new Color(255,45,45),
			new Color(255,5,5))

	override def forward() {
		super.forward();
		var c = getGroundColor();
		var nextColor:Color = null;
		for (i <- 0 to colors.length-1)
			if (colors(i).equals(c)) { 
				nextColor = colors(i+1);
			}
		setBrushColor(c);
		brushDown();
		brushUp();
	}

	override def run() {
		/* BEGIN SOLUTION */
		for (i <- 0 to 10; side <- 0 to 4) {
			for (step <- 0 to 8) {
				forward()
			}
			left()
		} 
		/* END SOLUTION */
	}
}
