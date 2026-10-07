#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

/* BEGIN REMOTE */
void solve(int src, int other, int dst);
void hanoi(int height, int src, int other, int dst);

void run()
{
  solve(getParamInt(0), getParamInt(1), getParamInt(2));
}

/* BEGIN TEMPLATE */
void solve(int src, int other, int dst)
{
  /* BEGIN SOLUTION */
  hanoi(getSlotSize(src), src, other, dst);
}

void hanoi(int height, int src, int other, int dst)
{
  if (height != 0) {
    hanoi(height - 1, src, dst, other);
    move(src, dst);
    hanoi(height - 1, other, src, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */
/* END REMOTE */
