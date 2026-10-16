#include "../../../../target/classes/resources/langages/c/RemotePancake.h"

/* BEGIN SOLUTION */
int getRankOf(int radius)
{
  int rank;
  for (rank = 0; rank < getStackSize(); rank++)
    if (getPancakeRadius(rank) == radius)
      return rank;
  return -1;
}

/* Whether the pancakes at these consecutive ranks are joined: p above p+1, or -(p+1) above -p */
int joined(int above, int below)
{
  int top    = isPancakeUpsideDown(above) ? -getPancakeRadius(above) : getPancakeRadius(above);
  int bottom = isPancakeUpsideDown(below) ? -getPancakeRadius(below) : getPancakeRadius(below);
  return bottom == top + 1;
}
/* END SOLUTION */

/* BEGIN TEMPLATE */
void solve() {
	/* BEGIN SOLUTION */
        // Joined pancakes are never separated: each block of joined pancakes behaves as a single pancake.
        // The pancakes from maxPos to the bottom are joined to the floor, and never moved again.
        int maxPos = getStackSize();
        while (1) {
          while (maxPos > 0 && getPancakeRadius(maxPos - 1) == maxPos && !isPancakeUpsideDown(maxPos - 1))
            maxPos--;
          if (maxPos == 0)
            return;

          int largestUp = -1;
          int rank;
          for (rank = 0; rank < maxPos; rank++)
            if (!isPancakeUpsideDown(rank) && getPancakeRadius(rank) > largestUp)
              largestUp = getPancakeRadius(rank);

          if (largestUp != -1) { // Case 1: p is the block ending with the largest rightside up pancake
            int p = getRankOf(largestUp);
            if (largestUp == maxPos) { // Case 1.C
              flip(p + 1);
              flip(maxPos);
            } else {
              int pPlus1 = getRankOf(largestUp + 1); // bottom of the next block, which is upside down
              if (pPlus1 > p) {                      // Case 1.A
                flip(pPlus1 + 1);
                flip(pPlus1 - p);
              } else { // Case 1.B
                flip(p + 1);
                flip(p - pPlus1);
              }
            }
          } else { // Case 2: all pancakes are upside down
            // Case 2.A: the largest radius such that -(radius+1) is above -radius without being joined to it
            int radius = maxPos - 1;
            while (radius > 0 && getRankOf(radius + 1) >= getRankOf(radius) - 1)
              radius--;
            if (radius > 0) {
              flip(getRankOf(radius + 1) + 1);
              flip(getRankOf(radius));
            } else { // Case 2.B: the blocks form -I. Once per block, flip them all, then all but the bottom one
              int blocks = 1;
              for (rank = 1; rank < maxPos; rank++)
                if (!joined(rank - 1, rank))
                  blocks++;
              int i;
              for (i = 0; i < blocks; i++) {
                flip(maxPos);
                int bottom = maxPos - 1;
                while (bottom > 0 && joined(bottom - 1, bottom))
                  bottom--;
                if (bottom > 0)
                  flip(bottom);
              }
            }
          }
        }
        /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  solve();
}
