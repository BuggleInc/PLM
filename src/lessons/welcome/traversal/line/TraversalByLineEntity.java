package lessons.welcome.traversal.line;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class TraversalByLineEntity extends SimpleBuggle {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    int cpt = 0;
    do {
      writeMessage(Integer.toString(cpt));
      nextStep();
      cpt++;
    } while (!endingPosition());
    writeMessage(Integer.toString(cpt));
  }

  public void nextStep()
  {
    int x = getX();
    int y = getY();
    if (x < getWorldWidth() - 1) {
      x++;
    } else {
      x = 0;
      if (y < getWorldHeight() - 1) {
        y++;
      } else {
        y = 0;
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

  public TraversalByLineEntity()
  {
    String reason = Game.i18n.tr("Use setPos(x,y) instead.");
    forbid("forward", reason);
    forbid("backward", reason);
    forbid("stepForward", reason);
    forbid("stepBackward", reason);
  }
}
