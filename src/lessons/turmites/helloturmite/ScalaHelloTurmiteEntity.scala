package lessons.turmites.helloturmite;

import java.awt.Color
import plm.universe.bugglequest.SimpleBuggle;
import lessons.turmites.universe.TurmiteWorld

class ScalaHelloTurmiteEntity extends SimpleBuggle {
		val allColors = Array(Color.white, Color.black, Color.blue, Color.cyan, Color.green, Color.orange, Color.red, 
			Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray);

	val STOP   = 0;
	val NOTURN = 1;
	val LEFT   = 2;
	val BACK   = 4;
	val RIGHT  = 8;

	val NEXT_COLOR = 0;
	val NEXT_MOVE  = 1;
	val NEXT_STATE = 2;


	/* BEGIN TEMPLATE */
	var state = 0;

	def step(colors:Array[Color], rule:Array[Array[Array[Int]]] ) {
		/* Your code comes here */
		/* BEGIN SOLUTION */
		var currentColor=0;
		
		val current = getGroundColor(); 
		for (i <- 0 to colors.length-1) 
			if (current == colors(i)) 
				currentColor = i;

		setBrushColor(colors( rule(state)(currentColor)(NEXT_COLOR) ));
		brushDown();
		brushUp();

		rule(state)(currentColor)(NEXT_MOVE) match {
		  case STOP   => /* nothing */
		  case NOTURN => /* no turn */ forward(); 
		  case LEFT   => left();   	   forward(); 
		  case RIGHT  => right();      forward(); 
		  case BACK   => back();       forward(); 
		  case _      => getGame().getLogger().log("Unknown turn command associated to i="+currentColor+": "+rule(state)(currentColor)(NEXT_MOVE));
		}

		state = rule(state)(currentColor)(NEXT_STATE);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() { 
		val nbSteps = getParam(0).asInstanceOf[Int];

		val rule = getParam(1).asInstanceOf[ Array[Array[Array[Int]]] ];

		var colors = new Array[Color] (rule.length);
		for (i <- 0 to rule.length-1)
			colors(i) = allColors(i);


		for (i <- 1 to nbSteps) {
			world.asInstanceOf[TurmiteWorld].stepDone();
			step(colors,rule);
		}
	}
}
