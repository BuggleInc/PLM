#include "RemoteBuggle.h"

int haveBaggle();

void run(){
	int i;
	for (i=0; i<7; i++) {
		if (haveBaggle())
			return 0;
		right();
		forward(1);
		left();
	}
}



#line 1 "MethodsReturning"
/* BEGIN TEMPLATE */
int haveBaggle(){
	/* BEGIN SOLUTION */
	int res = 0;
	int i;
	for (i=0; i<6; i++) {
		if (isOverBaggle())
			res = 1;
		forward(1);
	}
	for (i=0; i<6; i++)
		backward(1);
	return res;
	/* END SOLUTION */
}
/* END TEMPLATE */
