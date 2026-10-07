#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void run()
{
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  for (int i = 0; i < 7; i++)
    for (int side = 0; side < 4; side++) {
      for (int step = 0; step < 4; step++)
        forward(1);
      left();
      for (int step = 0; step < 2; step++)
        forward(1);
      right();
      for (int step = 0; step < 4; step++)
        forward(1);
      right();
      forward(1);
      forward(1);
      left();
      for (int step = 0; step < 4; step++)
        forward(1);
      left();
    }
  /* END SOLUTION */
  /* END TEMPLATE */
}
