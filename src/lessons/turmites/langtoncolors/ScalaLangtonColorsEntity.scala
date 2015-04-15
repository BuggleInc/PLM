package lessons.turmites.langtoncolors;

import java.awt.Color
import plm.universe.bugglequest.SimpleBuggle;
import lessons.turmites.universe.TurmiteWorld

class ScalaLangtonColorsEntity extends SimpleBuggle {
	val allColors = Array(Color.white, Color.black, Color.blue, Color.cyan, Color.green, Color.orange, Color.red, 
			Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray);

	/* BEGIN TEMPLATE */
	def step(rule:Array[Char], colors:Array[Color]) {
		/* BEGIN SOLUTION */
		var current = getGroundColor(); 
		for (i <- 0 to colors.length-1) {
			if (current == colors(i)) {
			  rule(i) match {
			    case 'L' => left()
			    case 'R' => right()
			    case _   => getGame().getLogger().log("Unknown command associated to i="+i+": "+rule(i));
			  }

			  setBrushColor(colors( (i+1) % colors.length ));
			  brushDown();
			  brushUp();

			  forward();

			  return;
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() { 
		val nbSteps = getParam(0).asInstanceOf[Int];
		val rule = getParam(1).asInstanceOf[Array[Char]];

		var colors = new Array[Color] (rule.length);
		for (i <- 0 to rule.length-1)
			colors(i) = allColors(i);

		for (i <- 1 to nbSteps) {
			world.asInstanceOf[TurmiteWorld].stepDone();
			step(rule,colors);
		}
	}
}
