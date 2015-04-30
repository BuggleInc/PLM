package lessons.turmites.turmitecreator;

import java.awt.Color

import plm.universe.bugglequest.SimpleBuggle
import lessons.turmites.universe.TurmiteWorld

class ScalaTurmiteCreatorEntity extends SimpleBuggle {
	val allColors = Array(Color.white, Color.yellow, Color.red, Color.cyan, Color.green, Color.orange, 
			Color.blue, Color.black,
			Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray);

	var state = 0;

	def step(colors:Array[Color]) {
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
	}

	/* BEGIN TEMPLATE */
	/* Do not change these definitions */
	val STOP   = 0;
	val NOTURN = 1;
	val LEFT   = 2;
	val BACK   = 4;
	val RIGHT  = 8;

	val NEXT_COLOR = 0;
	val NEXT_MOVE  = 1;
	val NEXT_STATE = 2;
	
	
	var rule:Array[Array[Array[Int]]] = null // Change this
	var nbSteps:Int = 0; // Change this

	/** init the rule array from a string defining a Langton's ant 
	 * 
	 *  You can use this method inside your init() method if you want 
	 *  to test langton's ant instead of full turmites.
	 */
	def initLangton(name:String) {
		val nbColors = name.length(); /* As many colors as letters in the ant's name */

		rule = new Array[Array[Array[Int]]] (1); /* one state only */
		rule(0) = new Array[Array[Int]] (nbColors); /* As many colors as letters in the ant's name */
		for (i <- 0 to nbColors-1) {
			rule(0)(i) = new Array[Int] (3); /* every command set has 3 elements */ 

			rule(0)(i)(NEXT_COLOR) = (i+1) % nbColors;

			if (name.charAt(i) == 'L') {
				rule(0)(i)(NEXT_MOVE) = LEFT;			
			} else if (name.charAt(i) == 'R') {
				rule(0)(i)(NEXT_MOVE) = RIGHT;
			} else {
				getGame().getLogger().log("Unknown command in your ant's name: "+name.charAt(i));
			}

			rule(0)(i)(NEXT_STATE) = 0; /* only one state */

			// println("{"+rule(0)(i)(NEXT_COLOR)+","+rule(0)(i)(NEXT_MOVE)+","+rule(0)(i)(NEXT_STATE)+"}");
		}
	}
	def init() {
		/* Your code comes here. */

		/* Something like 
		 *   nbSteps = 42;
		 *   rule = Array( Array( Array(0,NOTURN,0), Array(0, NOTURN, 0) ) ); 
		 * but with possibly more states (ie, bigger second dimension), and more color (ie bigger third -- internal -- dimension) 
		 * and naturally, less boring than this turmite doing absolutely nothing (runs forward endlessly).
		 */

		/* It can also be something like
		 *   nbSteps = 42;
		 *   initLangton("RL");
		 */

		/* remember to send your best creations for inclusion in the gallery */
		/* BEGIN SOLUTION */
		nbSteps = 8342;
		rule = Array( Array( Array(1, LEFT, 0), Array(1, LEFT, 1)), Array( Array(0, NOTURN, 0), Array(0, NOTURN, 1)));
		setX(8); setY(33);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() {
		init();

		var colors = new Array[Color] (rule(0).length);
		for (i <- 0 to rule(0).length-1) {
			if (i<allColors.length) {
				colors(i) = allColors(i);		    
			} else {
				/* allColors is too short; create the other colors randomly */
				var newColor:Color = null
						do {
							newColor = new Color(
									(Math.random()*255.0).asInstanceOf[Int] ,
									(Math.random()*255.0).asInstanceOf[Int] ,
									(Math.random()*255.0).asInstanceOf[Int] );
							for (j <- 0 to i-1) {
								if (colors(j) == newColor) {
									/* Damn we already picked that color; take another one please */
									newColor = null;
								}
							}
						} while (newColor == null);
			colors(i) = newColor;
			}
		}


		for (stepIt <- 1 to nbSteps) {
			step(colors);
			world.asInstanceOf[TurmiteWorld].stepDone();
		}
	}
}
