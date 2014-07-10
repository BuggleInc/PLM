#include "RemoteBuggle.h"

int isGroundWhite() {
	return getGroundColor()==white;
}

void run(){
	#line 1 "LoopDoWhile"
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	do {
		forward(1);
	} while (!isGroundWhite());
	/* END SOLUTION */
	/* END TEMPLATE */
}

