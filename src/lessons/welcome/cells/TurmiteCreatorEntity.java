package lessons.welcome.cells;

import java.awt.Color;

public class TurmiteCreatorEntity extends jlm.universe.bugglequest.SimpleBuggle {
	Color[] allColors = {Color.white, Color.black, Color.blue, Color.cyan, Color.green, Color.orange, Color.red, 
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
		case NOTURN: /* no turn */; break;
		case LEFT:   turnRight();   break;
		case RIGHT:  turnLeft();    break;
		case BACK:   turnBack();    break;
		default:
			System.out.println("Unknown turn command associated to i="+currentColor+": "+rule[state][currentColor][NEXT_MOVE]);
		}

		state = rule[state][currentColor][NEXT_STATE];

		forward();
}

/* BEGIN TEMPLATE */
final static int NOTURN = 1;
final static int LEFT   = 2;
final static int BACK   = 4;
final static int RIGHT  = 8;

int nbSteps; 
int[][][] rule; 
void init() {
	/* Your code comes here. Something like */
	/* rule = new int[][][] {{{0, NOTURN, 0}, {0, NOTURN, 0}}}; */
	/* but with possibly more states (ie, bigger second dimension), and more color (ie bigger third -- internal -- dimension) */
	/* and naturally, less boring than this turmite doing absolutely nothing */
	
	/* remember to send your best creations for inclusion in the gallery */
	/* BEGIN SOLUTION */
	nbSteps = 8342;
	rule = new int[][][] {{{1, LEFT, 0}, {1, LEFT, 1}}, {{0, NOTURN, 0}, {0, NOTURN, 1}}};
	setX(70); setY(33);
	/* END SOLUTION */
}
/* END TEMPLATE */

	@Override
	public void run() { 
		init();
		
		colors = new Color[rule.length];
		for (int i=0; i<rule.length; i++)
			colors[i] = allColors[i];

		for (int i=0;i<nbSteps;i++) {
			((TurmiteWorld)world).currStep = i;
			step();
		}
	}
}
