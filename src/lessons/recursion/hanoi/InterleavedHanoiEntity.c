#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void solve(int src1, int src2, int other, int dst);
void interleavedHanoi(int height, int src1, int src2, int other, int dst);
static void hanoi(int height, int src, int other, int dst);

/* BEGIN TEMPLATE */
void solve(int src1, int src2, int other, int dst)
{
  /* BEGIN SOLUTION */
  interleavedHanoi(getSlotSize(src1), src1, src2, other, dst);
}

void interleavedHanoi(int height, int src1, int src2, int other, int dst)
{
  if (height > 0) {
    hanoi(height - 1, src1, dst, other);
    move(src1, dst);
    hanoi(height - 1, src2, dst, src1);
    move(src2, dst);
    interleavedHanoi(height - 1, other, src1, src2, dst);
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
