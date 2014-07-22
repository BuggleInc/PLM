//RemoteTurtle


Color colors[] =  {cyan,blue,magenta,orange,yellow,green,lightGray,gray,darkGray,black,red};
int colorsLength = 11;
void current(int v) {
	if (v>=colorsLength || v < 0)
		setColor(colors[colorsLength -1]);
	setColor(colors[v]);
}


/* BEGIN TEMPLATE */
void tree(int steps, double length, double angle, double shrink)	{
	/* BEGIN SOLUTION */
	if (steps <= 0) {
		/* do nothing */
	} else {
		current(steps);
		forward(length);
		right(angle);
		tree(steps-1, length*shrink, angle, shrink);
		left(2*angle);
		tree(steps-1, length*shrink, angle, shrink);
		right(angle);
		current(steps);
		backward(length);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
void subtree(int steps, double length, double angle, double shrink)	{
	if (steps != 0) {
		setColor(black);
		forward(length);
		right(angle);
		subtree(steps-1, length*shrink, angle, shrink);
		left(2*angle);
		subtree(steps-1, length*shrink, angle, shrink);
		right(angle);
		backward(length);
	}
	/* END SOLUTION */
}

void run() {
	tree(getParamInt(0),getParamDouble(1),getParamDouble(2),getParamDouble(3));
}
