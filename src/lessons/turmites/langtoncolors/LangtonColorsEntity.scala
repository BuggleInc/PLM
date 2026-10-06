package lessons.turmites.langtoncolors;

import java.awt.Color
import plm.universe.bugglequest.SimpleBuggle;
import lessons.turmites.universe.TurmiteWorld

class LangtonColorsEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	val allColors = Array(Color.white, Color.black, Color.blue, Color.cyan, Color.green, Color.orange, Color.red, 
			Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray);

	/* BEGIN TEMPLATE */
	def step(rule:Array[Char], colors:Array[Color]): Unit = {
		/* BEGIN SOLUTION */
		val i = colors.indexOf(getGroundColor());
		if (i >= 0) {
			rule(i) match {
				case 'L' => left()
				case 'R' => right()
				case _   => System.out.println("Unknown command associated to i="+i+": "+rule(i));
			}

			setBrushColor(colors( (i+1) % colors.length ));
			brushDown();
			brushUp();

			stepForward();
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(): Unit = { 
		val nbSteps = getParamInt(0);
		val rule = getParamString(1).toCharArray;

		var colors = new Array[Color] (rule.length);
		for (i <- 0 to rule.length-1)
			colors(i) = allColors(i);

		for (i <- 1 to nbSteps) {
			stepDone();
			step(rule,colors);
		}
	}
	/* END REMOTE */
}
