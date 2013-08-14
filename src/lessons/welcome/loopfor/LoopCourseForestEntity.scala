package lessons.welcome.loopfor;

import java.awt.Color;
import jlm.universe.bugglequest.SimpleBuggle
import jlm.core.model.Game

class ScalaLoopCourseForestEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
	}

	var colors = Array(
			new Color(0,155,0),
			new Color(50,155,0),
			new Color(100,155,0),
			new Color(140,155,0),
			new Color(160,155,0),
			new Color(180,155,0),
			new Color(200,155,0),
			new Color(210,155,0))
	
	override def forward()  {
		if (!haveSeenError())
			super.forward();
		var c = getGroundColor();
		if (c.equals(Color.blue) && !haveSeenError()) {
			javax.swing.JOptionPane.showMessageDialog(null, Game.i18n.tr("You fall into water."), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
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
		for (i <- 0 to 7;  side <- 0 to 4){
				for (step <- 0 to 4)
					forward();
				left();
				for (step <- 0 to 2)
					forward();
				right();
				for (step <- 0 to 4)
					forward();
				right();
				forward();
				forward();
				left();
				for (step <- 0 to 4)
					forward();
				left();
		}
		/* END SOLUTION */
	}
}
