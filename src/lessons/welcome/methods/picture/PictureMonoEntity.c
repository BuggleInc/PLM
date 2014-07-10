#include "RemoteBuggle.h"

void mark();
void makeV();

#line 1 "PictureMono"
/* BEGIN TEMPLATE */
void run(){
	/* BEGIN SOLUTION */
	makeV();
	makeV();
	makeV();
	makeV();
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
/* END SOLUTION */

}
/* END TEMPLATE */
