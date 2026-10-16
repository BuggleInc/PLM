#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run(){
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	while (1) {
		char c = getIndicationBdr();
		if (c == 'R') {
			right();
                        step();
                } else if (c == 'L') {
                  left();
                  step();
                } else if (c == 'I') {
                  back();
                  step();
                } else if (c == 'A')
                  step();
                else if (c == 'B')
                  forward(2);
                else if (c == 'C')
                  forward(3);
                else if (c == 'Z')
                  stepBack();
                else if (c == 'Y')
                  backward(2);
                else if (c == 'X')
                  backward(3);
                else
                  return;
        }
        /* END SOLUTION */
        /* END TEMPLATE */
}
