#include "../../../../target/classes/resources/langages/c/RemoteHanoi.h"

void hanoi(int initialPos, int increasing);

/* BEGIN TEMPLATE */
void hanoi(int initialPos, int increasing)
{
  /* BEGIN SOLUTION */
  int small = initialPos;
  int count = 0;
  int pos1 = 0, pos2 = 0;
  do {
    if (count % 2 == 0) {
      int next = (small + 3 + (increasing ? 1 : -1)) % 3;
      move(small, next);
      small = next;
    }

    switch (small) {
      case 0:
        pos1 = 1;
        pos2 = 2;
        break;
      case 1:
        pos1 = 0;
        pos2 = 2;
        break;
      case 2:
        pos1 = 0;
        pos2 = 1;
        break;
    }
    if (count % 2 == 1) {
      if (getSlotRadius(pos1) > getSlotRadius(pos2))
        move(pos2, pos1);
      else
        move(pos1, pos2);
    }

    count++;
  } while (getSlotSize(pos1) != 0 || getSlotSize(pos2) != 0);
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  hanoi(getParamInt(0), getParamBoolean(1));
}
