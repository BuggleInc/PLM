#include "RemoteBuggle.h"

int isGroundWhite() {
	return getGroundColor()==white;
}

int main(){
	#line 1 "LoopDoWhile"
	/* BEGIN SOLUTION */
	do {
		forward(1);
	} while (!isGroundWhite());
	/* END SOLUTION */
	return 0;
}

