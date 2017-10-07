//RemoteTurtle

void drawHint();
void squareSide(int levels, double length);
void snowSquare (int levels, double length);

/* BEGIN TEMPLATE */
void pentaKoch(int levels, double length) {
	/* BEGIN SOLUTION */
	if (levels == 0) {
		forward(length);
	} else {
		pentaKoch(levels-1, length*.4);
		left(108);
		pentaKoch(levels-1, length*.4);
		right(72);
		pentaKoch(levels-1, length*.4);
		right(72);
		pentaKoch(levels-1, length*.4);
		right(72);
		pentaKoch(levels-1, length*.4);
		left(108);
		pentaKoch(levels-1, length*.4);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
void drawHint() {
	clear();
	hide();
	setColor(red);
	setPos(100,50);setHeading(0);
	pentaKoch(0,200);

	setPos(100,150); setHeading(0);
	pentaKoch(1,200);

	setPos(100,250); setHeading(0);
	pentaKoch(2,200);

	setPos(100,350); setHeading(0);
	pentaKoch(3,200);
}

void run() {
	pentaKoch(getParamInt(0),getParamDouble(1));
}
