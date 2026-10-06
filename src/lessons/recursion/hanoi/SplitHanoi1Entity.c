#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void solve(int src, int other, int dst1, int dst2);
void splitHanoi(int height, int src, int other, int dst1, int dst2);
static void moveDouble(int height, int src, int other1, int other2, int dst);

/* BEGIN TEMPLATE */
void solve(int src, int other, int dst1, int dst2)
{
  /* BEGIN SOLUTION */
  splitHanoi(getSlotSize(src) / 2, src, other, dst1, dst2);
}

void splitHanoi(int height, int src, int other, int dst1, int dst2)
{
  if (height > 0) {
    moveDouble(height - 1, src, dst1, dst2, other);
    move(src, dst1);
    move(src, dst2);
    splitHanoi(height - 1, other, src, dst1, dst2);
  }
}
static void moveDouble(int height, int src, int other1, int other2, int dst)
{
  if (height > 0) {
    moveDouble(height - 1, src, other1, dst, other2);
    move(src, other1);
    move(src, dst);
    move(other1, dst);
    moveDouble(height - 1, other2, src, other1, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  solve(getParamInt(0), getParamInt(1), getParamInt(2), getParamInt(3));
}
