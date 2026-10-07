package lessons.welcome.loopwhile;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class LoopWhileEntity extends SimpleBuggle {
  @Override public void forward(int i)
  {
    throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
  }

  @Override public void backward(int i)
  {
    throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
  }

  @Override
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    while (!isFacingWall())
      stepForward();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
