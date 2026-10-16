#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

int endingPosition();
void snakeStep() ;

/* BEGIN TEMPLATE */
void run() {
/* BEGIN SOLUTION */
	brushDown();
	while (!endingPosition()) {
		snakeStep();
	}
}
int endingPosition() {
	if (! isFacingWall())
		return 0;

	int res = 0;
	left();
	if (isFacingWall())
		res = 1;
	right();
	return res;
}

void snakeStep() {
	if (isFacingWall()) {
		if (getDirection() == EAST) {
			left();
                        step();
                        left();
                } else {
                  right();
                  step();
                  right();
                }
        } else {
          step();
        }

        /* END SOLUTION */
}
/* END TEMPLATE */
