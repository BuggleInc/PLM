//RemoteBuggle

void step(char* rule, Color* colors, int length);

Color allColors[] = {white, black, blue, cyan, green, orange, red, gray, magenta, darkGray, pink, lightGray};

void run(){
	int nbSteps = getParam();
	char* rule=(char*)malloc(sizeof(char)*256);
	int length = getParamLangtonColor1(rule);

	Color* colors = (Color*)malloc(sizeof(Color)*length);
	int i;
	for (i=0; i<length; i++)
		colors[i] = allColors[i];

	for (i=0;i<nbSteps;i++) {
		stepDone();
		step(rule,colors,length);
	}
	free(colors);
}

/* BEGIN TEMPLATE */
void step(char* rule, Color* colors, int length) {
	/* BEGIN SOLUTION */
	Color current = getGroundColor();
	int i;
	for (i=0;i<length;i++) {
		if (current==colors[i]) {
			switch (rule[i]) {
			case 'L':
				left();
				break;
			case 'R':
				right();
				break;
			default:
				printf("100 Unknown command associated to i=%d: %c\n",i,rule[i]);
				fflush(stdout);
				//getGame().getLogger().log("Unknown command associated to i="+i+": "+rule[i]);
			}
			setBrushColor(colors[(i+1) % length]);
			brushDown();
			brushUp();

			forward(1);

			return;
		}
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
