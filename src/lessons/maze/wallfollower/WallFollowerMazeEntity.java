package lessons.maze.wallfollower;

import plm.universe.Direction;

@SuppressWarnings("unused")
public class WallFollowerMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
  private Direction uselessVariableExistingJustToMakeSureThatEclipseWontRemoveTheImport; /* If removed, user code can't use directions easily */
  public WallFollowerMazeEntity() { forbidTeleportation(); }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    // Make sure we have a wall to the left
    left();
    while (!isFacingWall())
      stepForward();
    right();

    while (!isOverBaggle())
      stepHandOnWall();

    pickupBaggle();
  }

  public void stepHandOnWall()
  {
    // PRE: we have a wall on the left
    // POST: we still have the same wall on the left, are one step ahead

    while (!isFacingWall()) {
      stepForward();
      left(); // change to right to get a right follower
    }
    right(); // change to left to get a right follower
             /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
