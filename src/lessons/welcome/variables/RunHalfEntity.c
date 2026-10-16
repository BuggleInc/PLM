#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run(){
	/* BEGIN SOLUTION */
	int baggle = 0;
	int orange = 0;
	while (2 * baggle != orange + 1) {
          step();
          if (isOverBaggle())
            baggle++;
          if (isOverOrange())
            orange++;
        }
        /* END SOLUTION */
}



