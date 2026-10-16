package lessons.welcome.traversal;

import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

public class SnakeEntity extends SimpleBuggle {
  @Override
  /* BEGIN REMOTE */
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
        step();
        left();
      } else {
        right();
        step();
        right();
      }
    } else {
      step();
    }

    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
