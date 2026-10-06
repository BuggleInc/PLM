#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void gather(int height, int src, int mid, int dst);
void move3(int height, int src, int mid, int dst);

/* BEGIN TEMPLATE */
void gather(int height, int src, int mid, int dst)
{
  /* BEGIN SOLUTION */
  if (height > 0) {
    gather(height - 1, src, mid, dst);
    move(src, mid);
    move3(height - 1, dst, mid, src);
    move(mid, dst);
    move(mid, dst);
    move3(height - 1, src, mid, dst);
  }
}
void move3(int height, int src, int mid, int dst)
{
  if (height > 0) {
    move3(height - 1, src, dst, mid);
    move(src, dst);
    move(src, dst);
    move(src, dst);
    move3(height - 1, mid, src, dst);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  int src = getParamInt(0);
  int mid = getParamInt(1);
  int dst = getParamInt(2);
  gather(getSlotSize(src), src, mid, dst);
}
