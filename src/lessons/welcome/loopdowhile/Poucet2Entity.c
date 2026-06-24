#include "../../../../lib/resources/langages/c/include/RemoteBuggle.h"

/* Returns 1 if the current cell is a crossing (junction or corner in the maze) */
int crossing()
{
  int front = isFacingWall();
  int back  = isBackingWall();
  int left  = isWallOnLeft();
  int right = isWallOnRight();
  int open  = !front + !back + !left + !right;
  return open > 2 || (front != back) || (left != right);
}

/* Returns 1 if the exit (orange cell) has been reached */
int exitReached()
{
  char* c    = getGroundColorName();
  int result = strcmp(c, "255/200/0") == 0;
  free(c);
  return result;
}
void dave_forward(int steps)
{
  if (steps > 1) {
    printf("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.");
    exit(1);
  }
}
void dave_backward(int steps)
{
  if (steps > 1) {
    printf("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.");
    exit(1);
  }
}
void run()
{
  set_pre_forward(&dave_forward);
  set_pre_backward(&dave_backward);
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  while (!exitReached()) {
    int seen = 0;

    do {
      forward(1);
      if (isOverBaggle())
        seen++;
    } while (!crossing());

    if (seen > 2)
      left();
    else
      right();
  }
  forward(1);
  /* END SOLUTION */
  /* END TEMPLATE */
}
