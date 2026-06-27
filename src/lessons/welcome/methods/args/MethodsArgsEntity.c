#include "../../../../../lib/resources/langages/c/include/RemoteBuggle.h"

/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void move(int nbStep, int f)
{

  int i;
  if (f) {
    for (i = 0; i < nbStep; i++)
      stepForward();
  } else {
    for (i = 0; i < nbStep; i++)
      stepBackward();
  }
}
/* END SOLUTION */
/* END TEMPLATE */

static void go_forward(int steps)
{
  if (steps > 1) {
    printf("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.");
    exit(1);
  }
}
static void go_backward(int steps)
{
  if (steps > 1) {
    printf("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.");
    exit(1);
  }
}

void run(){
  set_pre_forward(go_forward);
  set_pre_backward(go_backward);
  if (getDirection() == NORTH) {
    move(getY(), 1);
  } else {
    move(getY(), 0);
  }
}
