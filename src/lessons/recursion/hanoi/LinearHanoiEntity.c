#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void linearHanoi(int height, int src, int mid, int dst);

/* BEGIN TEMPLATE */
void linearHanoi(int height, int src, int mid, int dst)
{
  /* BEGIN SOLUTION */
  if (height != 0) {
    linearHanoi(height - 1, src, mid, dst);
    move(src, mid);
    linearHanoi(height - 1, dst, mid, src);
    move(mid, dst);
    linearHanoi(height - 1, src, mid, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  linearHanoi(getSlotSize(getParamInt(0)), getParamInt(0), getParamInt(1), getParamInt(2));
}
