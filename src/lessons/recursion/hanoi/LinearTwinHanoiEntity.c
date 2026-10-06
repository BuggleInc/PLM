#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void solve(int src, int other, int dst);
void linearTwinHanoi(int height, int src, int mid, int dst);
static void gather(int height, int src, int mid, int dst);
static void scatter(int height, int src, int mid, int dst);
static void moveDouble(int height, int src, int mid, int dst);

/* move() is overridden in Java to forbid direct 0<->2 moves; C has no
 * inheritance, so the solution below calls this wrapper instead of move(). */
static void safeMove(int from, int to)
{
  if ((from == 0 && to == 2) || (from == 2 && to == 0)) {
    fprintf(stderr, "Sorry Dave, I cannot let you move disks between slots 0 and 2 directly. Use the intermediate slot in all moves.\n");
    exit(1);
  }
  move(from, to);
}

/* BEGIN TEMPLATE */
void solve(int src, int other, int dst)
{
  /* BEGIN SOLUTION */
  linearTwinHanoi(getSlotSize(src), src, other, dst);
}

void linearTwinHanoi(int height, int src, int mid, int dst)
{
  gather(height - 1, src, mid, dst);
  safeMove(src, mid);
  moveDouble(height - 1, dst, mid, src);
  safeMove(dst, mid);
  moveDouble(height - 1, src, mid, dst);
  safeMove(mid, src);
  moveDouble(height - 1, dst, mid, src);
  safeMove(mid, dst);
  scatter(height - 1, src, mid, dst);
}
static void gather(int height, int src, int mid, int dst)
{
  if (height > 0) {
    gather(height - 1, src, mid, dst);
    safeMove(src, mid);
    moveDouble(height - 1, dst, mid, src);
    safeMove(mid, dst);
    moveDouble(height - 1, src, mid, dst);
  }
}
static void scatter(int height, int src, int mid, int dst)
{
  if (height > 0) {
    moveDouble(height - 1, src, mid, dst);
    safeMove(src, mid);
    moveDouble(height - 1, dst, mid, src);
    safeMove(mid, dst);
    scatter(height - 1, src, mid, dst);
  }
}
static void moveDouble(int height, int src, int mid, int dst)
{
  if (height > 0) {
    moveDouble(height - 1, src, mid, dst);
    safeMove(src, mid);
    safeMove(src, mid);
    moveDouble(height - 1, dst, mid, src);
    safeMove(mid, dst);
    safeMove(mid, dst);
    moveDouble(height - 1, src, mid, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  solve(getParamInt(0), getParamInt(1), getParamInt(2));
}
