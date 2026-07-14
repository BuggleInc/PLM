package lessons.sort.dutchflag;

import lessons.sort.dutchflag.universe.DutchFlagEntity;
import lessons.sort.dutchflag.universe.DutchFlagWorld;

public class DutchFlagAlgoEntity extends DutchFlagEntity {

  public void run() { solve(); }

  /* BEGIN TEMPLATE */

  public final static int BLUE  = 0;
  public final static int WHITE = 1;
  public final static int RED   = 2;

  void solve()
  {
    /* BEGIN SOLUTION */
    int afterBlue   = 0;
    int beforeWhite = getSize() - 1;
    int beforeRed   = getSize() - 1;
    while (afterBlue <= beforeWhite) {

      switch (getColor(afterBlue)) {
        case BLUE:
          afterBlue++;
          break;
        case WHITE:
          swap(afterBlue, beforeWhite);
          beforeWhite--;
          break;
        case RED:
          swap(afterBlue, beforeWhite);
          swap(beforeRed, beforeWhite);
          beforeWhite--;
          beforeRed--;
      }
    }
    assertSorted();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
