#include "../../../../../target/classes/resources/langages/c/RemoteBuggle.h"

/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void move(int nbStep, int f)
{

  int i;
  if (f) {
    for (i = 0; i < nbStep; i++)
      step();
  } else {
    for (i = 0; i < nbStep; i++)
      stepBack();
  }
}
/* END SOLUTION */
/* END TEMPLATE */

void run()
{
  if (getDirection() == NORTH) {
    move(getY(), 1);
  } else {
    move(getY(), 0);
  }
}
