#include "RemoteBuggle.h"

int main(){
	#line 1 "LoopStairs"
	/* BEGIN SOLUTION */
	forward(1);
	forward(1);
	forward(1);
	left();
	int i;
	for (i = 0; i<8;i++) {
	    forward(1);
	    right();
	    forward(1);
	    left();
	}
	right();
	forward(1);
	forward(1);
	forward(1);

	/* END SOLUTION */
	return 0;
}
