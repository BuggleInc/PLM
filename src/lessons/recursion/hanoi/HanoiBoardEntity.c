//RemoteHanoi

void solve(int src, int dst, int other);
void solveRec(int src, int dst, int other, int height) ;

/* BEGIN TEMPLATE */
void solve(int src, int dst, int other) {
	solveRec(src, dst, other, getSlotSize(src));
}

void solveRec(int src, int dst, int other, int height) {
	/* BEGIN SOLUTION */
	if (height != 0) {
		solveRec(src,other,dst, height-1);
		move(src,dst);
		solveRec(other,dst,src, height-1);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

void run(){
	solve(getParam(0),getParam(1),getParam(2));
}
