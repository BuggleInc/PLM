#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void solve(int src, int other, int dst);
void linearTwinHanoi(int height, int src, int mid, int dst);
static void gather(int height, int src, int mid, int dst);
static void scatter(int height, int src, int mid, int dst);
static void moveDouble(int height, int src, int mid, int dst);

/* BEGIN TEMPLATE */
void solve(int src, int other, int dst)
{
  /* BEGIN SOLUTION */
  linearTwinHanoi(getSlotSize(src), src, other, dst);
}

void linearTwinHanoi(int height, int src, int mid, int dst)
{
  gather(height - 1, src, mid, dst);
  move(src, mid);
  moveDouble(height - 1, dst, mid, src);
  move(dst, mid);
  moveDouble(height - 1, src, mid, dst);
  move(mid, src);
  moveDouble(height - 1, dst, mid, src);
  move(mid, dst);
  scatter(height - 1, src, mid, dst);
}
static void gather(int height, int src, int mid, int dst)
{
  if (height > 0) {
    gather(height - 1, src, mid, dst);
    move(src, mid);
    moveDouble(height - 1, dst, mid, src);
    move(mid, dst);
    moveDouble(height - 1, src, mid, dst);
  }
}
static void scatter(int height, int src, int mid, int dst)
{
  if (height > 0) {
    moveDouble(height - 1, src, mid, dst);
    move(src, mid);
    moveDouble(height - 1, dst, mid, src);
    move(mid, dst);
    scatter(height - 1, src, mid, dst);
  }
}
static void moveDouble(int height, int src, int mid, int dst)
{
  if (height > 0) {
    moveDouble(height - 1, src, mid, dst);
    move(src, mid);
    move(src, mid);
    moveDouble(height - 1, dst, mid, src);
    move(mid, dst);
    move(mid, dst);
    moveDouble(height - 1, src, mid, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  solve(getParamInt(0), getParamInt(1), getParamInt(2));
}
