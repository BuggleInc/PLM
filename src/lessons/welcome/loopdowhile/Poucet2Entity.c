#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run()
{
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  while (!exitReached()) {
    int seen = 0;

    do {
      step();
      if (isOverBaggle())
        seen++;
    } while (!crossing());

    if (seen > 2)
      left();
    else
      right();
  }
  step();
  /* END SOLUTION */
  /* END TEMPLATE */
}
