#include "RemoteBuggle.h"

int crossing() {
	return getX()%5== 1 && getY()%5==1;
}
int exitReached() {
	return getGroundColor()==orange;
}

int main(){
	#line 1 "Poucet"
	/* BEGIN SOLUTION */
	/* END SOLUTION */
	return 0;
}
