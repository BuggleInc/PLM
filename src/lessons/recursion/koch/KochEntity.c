//RemoteTurtle

void snowFlake (int levels, double length);
void snowSide(int levels, double length);
void drawHint();

/* BEGIN TEMPLATE */
void snowFlake (int levels, double length) {
	snowSide(levels, length);
	right(120);
	setColor(blue);
	snowSide(levels, length);
	right(120);
	setColor(orange);
	snowSide(levels, length);
	right(120);
}
void snowSide(int levels, double length) {
	/* BEGIN SOLUTION */
	if (levels == 0) {
		forward(length);
	} else {
		snowSide(levels-1, length/3);
		left(60);
		snowSide(levels-1, length/3);
		right(120);
		snowSide(levels-1, length/3);
		left(60);
		snowSide(levels-1, length/3);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

void run() {
	snowFlake(getParamInt(0),getParamDouble(1));
}

void drawHint() {
	clear();
	hide();
	setColor(red);
	setPos(100,50);setHeading(0);
	snowSide(0,200);

	setPos(100,150); setHeading(0);
	snowSide(1,200);

	setPos(100,250); setHeading(0);
	snowSide(2,200);

	setPos(100,350); setHeading(0);
	snowSide(3,200);
}
