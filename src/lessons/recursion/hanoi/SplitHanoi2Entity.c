#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void solve(int src, int other, int dst1, int dst2);
void splitHanoi(int height, int src, int other, int dst1, int dst2);
static void hanoi(int height, int src, int other, int dst);

/* BEGIN TEMPLATE */
void solve(int src, int other, int dst1, int dst2)
{
  /* BEGIN SOLUTION */
  splitHanoi(getSlotSize(src) / 2, src, other, dst1, dst2);
}

void splitHanoi(int height, int src, int other, int dst1, int dst2)
{
  if (height > 0) {
    splitHanoi(height - 1, src, dst1, dst2, other);
    move(src, dst1);
    hanoi(height - 1, dst2, src, dst1);
    move(src, dst2);
    hanoi(height - 1, other, src, dst2);
  }
}
static void hanoi(int height, int src, int other, int dst)
{
  if (height > 0) {
    hanoi(height - 1, src, dst, other);
    move(src, dst);
    hanoi(height - 1, other, src, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  solve(getParamInt(0), getParamInt(1), getParamInt(2), getParamInt(3));
}
