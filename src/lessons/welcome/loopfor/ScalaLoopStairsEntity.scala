package lessons.welcome.loopfor;

import java.awt.Color;
import plm.universe.bugglequest.SimpleBuggle
import plm.core.model.Game

class ScalaLoopStairsEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	var colors = Array(
			Color.blue,    Color.cyan, Color.green,  Color.yellow,
			Color.orange,  Color.red,  Color.magenta,Color.pink)
	
	var step = -3
	override def forward() {
		super.forward();
		if (step<0 || step%2 == 1 || (step/2)>=colors.length) {
			if (step < 0)
				setBrushColor(Color.lightGray);
			else if ((step/2)>=colors.length)
				setBrushColor(Color.pink);
			else
				setBrushColor(colors((step/2)%colors.length));
			brushDown();
			brushUp();
		}
		step += 1; 
	}

	override def run() {
		/* BEGIN SOLUTION */
		forward();
		forward();
		forward();
		left();
		for (i <- 1 to 8) { 
			forward();
			right();
			forward();
			left();
		}
		right();
		forward();
		forward();
		forward();
		/* END SOLUTION */
	}
}
