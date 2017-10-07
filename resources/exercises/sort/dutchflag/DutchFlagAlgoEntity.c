//RemoteFlag

#define BLUE  0
#define WHITE  1
#define RED  2


/* BEGIN TEMPLATE */
void solve() {
	/* BEGIN SOLUTION */
	int afterBlue=0;
	int beforeWhite=getSize()-1;
	int beforeRed=getSize()-1;
	while (afterBlue <= beforeWhite) {
		switch (getColor(afterBlue)) {
		case BLUE:
			afterBlue++;
			break;
		case WHITE:
			swap(afterBlue, beforeWhite);
			beforeWhite --;
			break;
		case RED:
			swap(afterBlue, beforeWhite);
			swap(beforeRed, beforeWhite);
			beforeWhite--;
			beforeRed--;
		}
	}
	assertSorted();
	/* END SOLUTION */
}
/* END TEMPLATE */

void run(){
	solve();
}
