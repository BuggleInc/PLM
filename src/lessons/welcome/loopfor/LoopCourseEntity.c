#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run()
{
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  for (int i = 0; i < 10; i++)
    for (int side = 0; side < 4; side++) {
      for (int k = 0; k < 8; k++)
        step();
      left();
    }
  /* END SOLUTION */
  /* END TEMPLATE */
}
