#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void linearHanoi(int height, int src, int mid, int dst);

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
void linearHanoi(int height, int src, int mid, int dst)
{
  /* BEGIN SOLUTION */
  if (height != 0) {
    linearHanoi(height - 1, src, mid, dst);
    safeMove(src, mid);
    linearHanoi(height - 1, dst, mid, src);
    safeMove(mid, dst);
    linearHanoi(height - 1, src, mid, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  linearHanoi(getSlotSize(getParamInt(0)), getParamInt(0), getParamInt(1), getParamInt(2));
}
