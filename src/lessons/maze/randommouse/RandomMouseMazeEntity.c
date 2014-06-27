#include "RemoteBuggle.h"

void run();
int random3();

int main(){
	run();
	return 0;
}


#line 1 "RandomMouseMaze.c"
/* BEGIN TEMPLATE */
void run() {
	// Your code here =)
	/* BEGIN SOLUTION */
	while (!isOverBaggle()) {
		switch(random3()) {
		case 0:
			if (!isFacingWall()){
				forward(1);
			}
			break;
		case 1:
			left();
			break;
		case 2:
			right();
			break;
		}
	}
	//pickupBaggle();
	/* END SOLUTION */
}
/* END TEMPLATE */

int random3() {
	return rand()%3;
}
