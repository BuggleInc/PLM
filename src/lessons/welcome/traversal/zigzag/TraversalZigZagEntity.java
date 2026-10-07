package lessons.welcome.traversal.zigzag;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class TraversalZigZagEntity extends SimpleBuggle {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    int cpt = 0;
    writeMessage(Integer.toString(cpt));
    while (!endingPosition()) {
      nextStep();
      cpt++;
      writeMessage(Integer.toString(cpt));
    }
  }

  public void nextStep()
  {
    int x = getX();
    int y = getY();

    if (y % 2 == 0) {
      if (x < getWorldWidth() - 1) {
        x++;
      } else if (y < getWorldHeight() - 1) {
        y++;
      }
    } else {
      if (0 < x) {
        x--;
      } else if (y < getWorldHeight() - 1) {
        y++;
      }
    }

    setPos(x, y);
  }

  public boolean endingPosition()
  {
    return (getX() == getWorldWidth() - 1) && (getY() == getWorldHeight() - 1);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */

  public TraversalZigZagEntity()
  {
    String reason = Game.i18n.tr("Use setPos(x,y) instead.");
    forbid("forward", reason);
    forbid("backward", reason);
    forbid("stepForward", reason);
    forbid("stepBackward", reason);
  }
}
