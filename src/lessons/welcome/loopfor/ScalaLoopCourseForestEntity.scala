package lessons.welcome.loopfor;

import java.awt.Color;
import plm.universe.bugglequest.SimpleBuggle
import plm.core.model.Game

class ScalaLoopCourseForestEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}
	override def backward() {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, you cannot run backward that way. Exercising is hard enough -- please don't overplay."));
	}

	var colors = Array(
			new Color(0,155,0),
			new Color(50,155,0),
			new Color(100,155,0),
			new Color(140,155,0),
			new Color(160,155,0),
			new Color(180,155,0),
			new Color(200,155,0),
			new Color(210,155,0), 
			Color.red)
	
	override def forward()  {
		if (!haveSeenError())
			super.forward();
		var c = getGroundColor();
		if (c.equals(Color.blue)) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, getGame().i18n.tr("You fall into water."), getGame().i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		} else {
			var nextColor:Color = null;
			for (i <- 0 to colors.length-1)
				if (colors(i).equals(c)) { 
					if (i==colors.length-1)
						nextColor = colors(i);
					else
						nextColor = colors(i+1);
				}
			setBrushColor(nextColor);
			brushDown();
			brushUp();
		}
	}

	override def run() {
		/* BEGIN SOLUTION */
		for (i <- 1 to 7;  side <- 1 to 4){
				for (step <- 1 to 4)
					forward();
				left();
				for (step <- 1 to 2)
					forward();
				right();
				for (step <- 1 to 4)
					forward();
				right();
				forward();
				forward();
				left();
				for (step <- 1 to 4)
					forward();
				left();
		}
		/* END SOLUTION */
	}
}
