#include "RemoteTurtle.h"

void run();
void house(int len);

int main(){
	run();
	return 0;
}


#line 1 "House.c"
/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	house(100);
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
