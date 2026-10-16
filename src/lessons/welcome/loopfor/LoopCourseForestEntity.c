#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run()
{
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  for (int i = 0; i < 7; i++)
    for (int side = 0; side < 4; side++) {
      for (int k = 0; k < 4; k++)
        step();
      left();
      for (int k = 0; k < 2; k++)
        step();
      right();
      for (int k = 0; k < 4; k++)
        step();
      right();
      step();
      step();
      left();
      for (int k = 0; k < 4; k++)
        step();
      left();
    }
  /* END SOLUTION */
  /* END TEMPLATE */
}
