package lessons.welcome.traversal;

import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

public class SnakeEntity extends SimpleBuggle {
  @Override
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    brushDown();
    while (!endingPosition()) {
      snakeStep();
    }
  }
  boolean endingPosition()
  {
    if (!isFacingWall())
      return false;

    boolean res = false;
    left();
    if (isFacingWall())
      res = true;
    right();
    return res;
  }

  void snakeStep()
  {
    if (isFacingWall()) {
      if (getDirection() == Direction.EAST) {
        left();
        stepForward();
        left();
      } else {
        right();
        stepForward();
        right();
      }
    } else {
      stepForward();
    }

    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
