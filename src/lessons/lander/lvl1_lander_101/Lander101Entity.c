#include "../../../../target/classes/resources/langages/c/RemoteLander.h"

void step();

void run()
{
  while (isFlying()) {
    step();
    simulateStep();
  }
}

/* BEGIN TEMPLATE */
void step()
{
  /* BEGIN SOLUTION */
  setDesiredThrust(getSpeedY() < -9 ? 4 : 3);
  /* END SOLUTION */
}
/* END TEMPLATE */
