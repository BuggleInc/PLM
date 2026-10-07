package lessons.welcome.traversal.diagonal;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class TraversalDiagonalEntity extends SimpleBuggle {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  int diag = 0;
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

    if ((x + 1 < getWorldWidth()) && (y > 0)) {
      x++;
      y--;
    } else if (diag + 1 < getWorldHeight()) {
      diag++;
      y = diag;
      x = 0;
    } else {
      diag++;
      x = diag - (getWorldWidth() - 1);
      y = diag - x;
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

  public TraversalDiagonalEntity()
  {
    String reason = Game.i18n.tr("Use setPos(x,y) instead.");
    forbid("forward", reason);
    forbid("backward", reason);
    forbid("stepForward", reason);
    forbid("stepBackward", reason);
  }
}
