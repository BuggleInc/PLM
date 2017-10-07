//RemoteBuggle

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

void step(Color* colors, int colorsLength, int*** rule, int dim1, int dim2, int dim3 ) {
	int currentColor=0;
	/* Your code comes here */
	/* BEGIN SOLUTION */
	Color current = getGroundColor();
	int i;
	for (i=0;i<colorsLength;i++)
		if (current==colors[i])
			currentColor = i;

	setBrushColor(colors[ rule[state][currentColor][NEXT_COLOR] ]);
	brushDown();
	brushUp();

	switch (rule[state][currentColor][NEXT_MOVE]) {
	case STOP:   /* nothing */;            break;
	case NOTURN: /* no turn */; forward(1); break;
	case LEFT:   left();   	forward(1); break;
	case RIGHT:  right();   forward(1); break;
	case BACK:   back();    forward(1); break;
	default:
		printf("100 Unknown command associated to i=%d: %c\n",currentColor,rule[state][currentColor][NEXT_MOVE]);
		fflush(stdout);
	}

	state = rule[state][currentColor][NEXT_STATE];

	/* END SOLUTION */
}
/* END TEMPLATE */

void run() {
	int nbSteps = (int)getParam(0);
	Color* colors;
	int dim1,dim2,dim3;
	int*** rule = getParamHelloTurmite1(&dim1,&dim2,&dim3);

	colors = (Color*)malloc(sizeof(Color)*dim1);  //new Color[rule.length];
	int i;
	for (i=0; i<dim1; i++)
		colors[i] = allColors[i];

	for (i=0;i<nbSteps;i++) {
		stepDone();
		step(colors,dim1,rule,dim1, dim2, dim3);
	}
}

