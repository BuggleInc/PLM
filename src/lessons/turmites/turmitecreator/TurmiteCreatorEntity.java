package lessons.turmites.turmitecreator;

import java.awt.Color;
import java.io.BufferedWriter;

public class TurmiteCreatorEntity extends plm.universe.bugglequest.SimpleBuggle {
	Color[] allColors = {Color.white, Color.yellow, Color.red, Color.cyan, Color.green, Color.orange, 
			Color.blue, Color.black,
			Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray};

	/* Do not change these definitions */

	final static int NEXT_COLOR = 0;
	final static int NEXT_MOVE  = 1;
	final static int NEXT_STATE = 2;


	Color[] colors; 
	int state = 0;

	public void step() {
		int currentColor=0;
		Color current = getGroundColor(); 
		for (int i=0;i<colors.length;i++) 
			if (current.equals(colors[i])) 
				currentColor = i;

		setBrushColor(colors[ rule[state][currentColor][NEXT_COLOR] ]);
		brushDown();
		brushUp();

		switch (rule[state][currentColor][NEXT_MOVE]) {
		case STOP:   /* nothing */;            break;
		case NOTURN: /* no turn */; forward(); break;
		case LEFT:   left();   	forward(); break;
		case RIGHT:  right();   forward(); break;
		case BACK:   back();    forward(); break;
		default:
			getGame().getLogger().log("Unknown turn command associated to i="+currentColor+": "+rule[state][currentColor][NEXT_MOVE]);
		}

		state = rule[state][currentColor][NEXT_STATE];
	}

	/* BEGIN TEMPLATE */
	final static int STOP   = 0;
	final static int NOTURN = 1;
	final static int LEFT   = 2;
	final static int BACK   = 4;
	final static int RIGHT  = 8;

	int nbSteps; 
	int[][][] rule;

	/** init the rule array from a string defining a Langton's ant 
	 * 
	 *  You can use this method inside your init() method if you want 
	 *  to test langton's ant instead of full turmites.
	 */
	void initLangton(String name) {
		int nbColors = name.length(); /* As many colors as letters in the ant's name */

		rule = new int[1][][]; /* one state only */
		rule[0] = new int[nbColors][]; /* As many colors as letters in the ant's name */
		for (int i=0; i<nbColors; i++) {
			rule[0][i] = new int[3]; /* every command set has 3 elements */ 

			rule[0][i][NEXT_COLOR] = (i+1) % nbColors;

			if (name.charAt(i) == 'L') {
				rule[0][i][NEXT_MOVE] = LEFT;			
			} else if (name.charAt(i) == 'R') {
				rule[0][i][NEXT_MOVE] = RIGHT;
			} else {
				getGame().getLogger().log("Unknown command in your ant's name: "+name.charAt(i));
			}

			rule[0][i][NEXT_STATE] = 0; /* only one state */

			//		getGame().getLogger().log("{"+rule[0][i][NEXT_COLOR]+","+rule[0][i][NEXT_MOVE]+","+rule[0][i][NEXT_STATE]+"}");
		}
	}
	void init() {
		/* Your code comes here. */

		/* Something like 
		 *   nbSteps = 42;
		 *   rule = new int[][][] {{{0, NOTURN, 0}, {0, NOTURN, 0}}}; 
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
		rule = new int[][][] {{{1, LEFT, 0}, {1, LEFT, 1}}, {{0, NOTURN, 0}, {0, NOTURN, 1}}};
		setX(8); setY(33);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	@Override
	public void run() { 
		init();

		colors = new Color[rule[0].length];
		int i;
		for (i=0; i<Math.min(rule[0].length,allColors.length); i++)
			colors[i] = allColors[i];
		for (; i<rule[0].length; i++) { /* allColors is too short; create the other colors randomly */
			Color newColor = null;
			do {
				newColor = new Color(
						(int)(Math.random()*255.) ,
						(int)(Math.random()*255.) ,
						(int)(Math.random()*255.) );
				for (int j=0;j<i;j++) {
					if (colors[j].equals(newColor)) {
						/* Damn we already picked that color; take another one please */
						newColor = null;
					}
				}
			} while (newColor == null);
			colors[i] = newColor;
		}


		for (int step=0;step<nbSteps;step++) {
			step();
			stepDone();
		}
	}
	
	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		switch(num){
		case 201:
			stepDone();
			break;
		default:
			super.command(command, out);
		}
	}
	
	public void stepDone(){
		((lessons.turmites.universe.TurmiteWorld)world).stepDone();
	}
}
