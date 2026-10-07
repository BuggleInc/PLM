#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run()
{
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  for (int i = 0; i < 10; i++)
    for (int side = 0; side < 4; side++) {
      for (int step = 0; step < 8; step++)
        forward(1);
      left();
    }
  /* END SOLUTION */
  /* END TEMPLATE */
}
