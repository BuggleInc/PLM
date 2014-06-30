#include "RemoteTurtle.h"

void run();
void house(int len);

int main(){
	run();
	return 0;
}


#line 1 "HouseThree.c"
/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int i;
	for (i=0;i<4;i++) {
		house(30);
		penUp();
		right(90);
		forward(50);
		left(90);
		penDown();
	 }
}

void house(int len) {
	forward(len);
	int i;
	right(30);
	for (i = 0; i < 3; i++) {
		forward(len);
		right(120);
	}

	right(60);
	for (i = 0; i < 3; i++) {
		forward(len);
		right(90);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
