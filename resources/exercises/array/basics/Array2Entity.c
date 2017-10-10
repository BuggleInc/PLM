//RemoteBuggle

void mark(Color c);
void makeLine(Color colors[], int colorsLength);

/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void mark(Color c){
	setBrushColor(c);
	brushDown();
	brushUp();
}

void run() {
	Color* colors = (Color*) malloc(sizeof(Color)*getWorldHeight());
	int i;
	/* read the colors */
	colors[0]=getGroundColor();
	for (i=1;i<getWorldHeight();i++) {
		forward(1);
		colors[i]=getGroundColor();
	}
	backward(getWorldHeight()-1);

	/* Duplicate the pattern */
	for (i=1; i<getWorldWidth();i++) {
		left();
		forward(1);
		right();
		makeLine(colors,getWorldHeight());
	}
	free(colors);
}



void makeLine(Color colors[], int colorsLength) {
	int offset = (int)readMessage()[0];
	mark(colors[(0+offset)%colorsLength]);
	int i;
	for (i=1;i<getWorldWidth();i++) {
		forward(1);
		mark(colors[(i+offset)%colorsLength]);
	}
	backward(getWorldHeight()-1);
}
/* END SOLUTION */
/* END TEMPLATE */
