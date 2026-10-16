#include "../../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void mark();
void makeV();

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
	forward(2);
	mark();

        step();
        left();
        step();
        mark();

        stepBack();
        right();
        step();
        mark();

        forward(2);
        left();
        /* END SOLUTION */
}
/* END TEMPLATE */
