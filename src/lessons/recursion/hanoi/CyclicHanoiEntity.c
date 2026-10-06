#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void clockwise(int height, int src, int mid, int dst);
void anti(int height, int src, int mid, int dst);

/* move() is overridden in Java to call cyclicMove(); C has no inheritance,
 * so the solution below calls this wrapper instead of move(). */
static void doMove(int src, int dst)
{
  cyclicMove(src, dst);
}

/* BEGIN TEMPLATE */
void clockwise(int height, int src, int mid, int dst)
{
  /* BEGIN SOLUTION */
  if (height > 0) {
    anti(height - 1, src, dst, mid);
    doMove(src, dst);
    anti(height - 1, mid, src, dst);
  }
}
void anti(int height, int src, int mid, int dst)
{
  if (height > 0) {
    anti(height - 1, src, mid, dst);
    doMove(src, mid);
    clockwise(height - 1, dst, mid, src);
    doMove(mid, dst);
    anti(height - 1, src, mid, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  clockwise(getSlotSize(getParamInt(0)), getParamInt(0), getParamInt(1), getParamInt(2));
}
