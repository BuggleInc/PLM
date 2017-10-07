//RemoteBuggle

void growFlowers();
void boxes();
void LRforward(int steps);
void RLforward(int steps);
void line(Color colors[],int colorsLength, int returnBack);
void makeFlower(Color c);

void run(){
	growFlowers();
}

/* BEGIN SOLUTION */
void makeFlower(Color c) {
	setBrushColor(c);
	brushDown();
	forward(2);
	backward(1);
	left();
	forward(1);
	backward(2);
	forward(1);
	setBrushColor(yellow);
	brushUp();
	right();
	backward(1);
}

void line(Color colors[],int colorsLength, int returnBack) {
	int first = 1;
	int i;
	for (i=0;i<colorsLength;i++) {
		if (!first)
			forward(4);
		makeFlower(colors[i]);
		first = 0;
	}

	if (returnBack)
		backward(4*(colorsLength-1));
}
void RLforward(int steps) {
	right();
	forward(steps);
	left();
}
void LRforward(int steps) {
	left();
	forward(steps);
	right();
}
void boxes() {
	Color tab[] = {red, cyan};
	line(tab,2,1);
	RLforward(4);
	Color tab2[] = {pink, green};
	line(tab2,2,1);

	LRforward(2);
	forward(2);
	Color tab3[] ={orange,blue,orange};
	line(tab3,3,0);

	LRforward(2);
	backward(2);

	Color tab4[]={red, cyan};
	line(tab4,2,1);
	RLforward(4);
	Color tab5[]={pink, green};
	line(tab5,2,1);
}

void growFlowers() {
	boxes();
	LRforward(1);
	backward(8);
	RLforward(5);
	boxes();
}
/* END SOLUTION */
