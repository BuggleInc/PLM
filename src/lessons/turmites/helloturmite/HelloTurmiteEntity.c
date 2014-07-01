#include "RemoteBuggle.h"

Color allColors[] = {white, black, blue, cyan, green, orange, red, gray, magenta, darkGray, pink, lightGray};

/* BEGIN TEMPLATE */
#define STOP    0 /* for example */
/* BEGIN HIDDEN */
#define NOTURN  1
#define LEFT    2
#define BACK    4
#define RIGHT   8

#define NEXT_COLOR  0
#define NEXT_MOVE   1
#define NEXT_STATE  2
/* END HIDDEN */

int state = 0;

void step(Color colors[], int rule[][][] ) {
	int currentColor=0;
	/* Your code comes here */
	/* BEGIN SOLUTION */
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
		System.out.println("Unknown turn command associated to i="+currentColor+": "+rule[state][currentColor][NEXT_MOVE]);
	}

	state = rule[state][currentColor][NEXT_STATE];

	/* END SOLUTION */
}
/* END TEMPLATE */

void run() {
	int nbSteps = (Integer)getParam(0);
	Color colors[];
	int rule[][][];

	rule = ((int[][][])getParam(1));

	colors = (Color*)malloc(sizeof(Color)*rule.length);  //new Color[rule.length];
	for (int i=0; i<rule.length; i++)
		colors[i] = allColors[i];

	for (int i=0;i<nbSteps;i++) {
		((lessons.turmites.universe.TurmiteWorld)world).stepDone();
		step(colors,rule);
	}
}
