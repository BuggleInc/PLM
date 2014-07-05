#include "RemoteBuggle.h"

void mark();
void makeV();
void makePattern();
void makeLine(int count);
void nextLine();


#line 1 "PictureMono2"
/* BEGIN TEMPLATE */

int main(){
	/* BEGIN SOLUTION */
	int i;
	for (i=0; i<3;i++) {
		makeLine(3);
		nextLine();
	}
	return 0;
}
void mark() {
	brushDown();
	brushUp();
}

void makeV() {
	forward(1);
	mark();

	forward(1);
	left();
	forward(1);
	mark();

	backward(1);
	right();
	forward(1);
	mark();

	forward(1);
	left();
}

void makePattern() {
	makeV();
	makeV();
	makeV();
	makeV();
	forward(5);
}

void makeLine(int count){
	int i;
	for (i=0; i<count;i++)
		makePattern();
	backward(count*5);
}

void nextLine() {
	left();
	forward(5);
	right();
	/* END SOLUTION */
}

/* END TEMPLATE */
