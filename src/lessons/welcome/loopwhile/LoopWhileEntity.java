package lessons.welcome.loopwhile;

import plm.universe.bugglequest.SimpleBuggle;

public class LoopWhileEntity extends SimpleBuggle {
  public LoopWhileEntity() { forbidMultiStep(); }

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
