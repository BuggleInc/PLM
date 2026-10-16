#include "../../../../../target/classes/resources/langages/c/RemoteBuggle.h"

/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void goAndGet() {
	int i = 0;
	while (!isOverBaggle()) {
		i++;
                step();
        }
        pickupBaggle();
        while (i > 0) {
          stepBack();
          i--;
        }
        dropBaggle();
}
/* END SOLUTION */
/* END TEMPLATE */

void run()
{
  int i = 0;
  for (i = 0; i < 7; i++) {
    goAndGet();
    right();
    step();
    left();
  }
}
