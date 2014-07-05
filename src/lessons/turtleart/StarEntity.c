#include "RemoteTurtle.h"

void run();
void branch(int size);

int main(){
	run();
	return 0;
}


#line 1 "Star.c"
/* BEGIN TEMPLATE */


void run() {
	/* BEGIN SOLUTION */
	#define BRANCH_COUNT 5
	int i;
	for (i = 0; i < BRANCH_COUNT; i++){
		branch(50);
	}

}
void branch(int size) {
	forward(size);
	right(360 / BRANCH_COUNT);
	forward(size);
	int i;
	for (i = 0; i < 2; i++)
		left(360 / BRANCH_COUNT);
	/* END SOLUTION */
}
/* END TEMPLATE */
