package lessons.welcome.loopfor;

import java.awt.Color;
import plm.universe.bugglequest.SimpleBuggle
import plm.core.model.Game

class ScalaLoopStairsEntity extends SimpleBuggle {
	override def forward(i: Int)  {
		for (i <- 1 to i) {
			stepForward()
		}
	}

	var colors = Array(
			Color.blue,    Color.cyan, Color.green,  Color.yellow,
			Color.orange,  Color.red,  Color.magenta,Color.pink)
	
	var inTeerNal_Steep_Count = -3
	override def stepForward() {
		super.stepForward();
		if (inTeerNal_Steep_Count<0 || inTeerNal_Steep_Count%2 == 1 || (inTeerNal_Steep_Count/2)>=colors.length) {
			if (inTeerNal_Steep_Count < 0)
				setBrushColor(Color.lightGray);
			else if ((inTeerNal_Steep_Count/2)>=colors.length)
				setBrushColor(Color.pink);
			else
				setBrushColor(colors((inTeerNal_Steep_Count/2)%colors.length));
			brushDown();
			brushUp();
		}
		inTeerNal_Steep_Count += 1; 
	}

	override def run() {
		/* BEGIN SOLUTION */
		forward(3);
		left();
		for (i <- 1 to 8) { 
			stepForward();
			right();
			stepForward();
			left();
		}
		right();
		forward(3);
		/* END SOLUTION */
	}
}
