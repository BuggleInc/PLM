package lessons.maze.island;

import plm.universe.Direction;

public class IslandMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
  public IslandMazeEntity() { forbidTeleportation(); }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    int state = 0;
    this.setDirection(this.chosenDirection);
    while (!isOverBaggle()) {
      switch (state) {
        case 0: // North runner mode
          while (!isFacingWall())
            stepForward();

          this.right(); // make sure that we have a left wall
          state = 1;    // time to enter the Left Follower mode
          break;
        case 1:                  // Left Follower Mode
          this.stepHandOnWall(); // follow the left wall
          if (isChosenDirectionFree() && (getDirection() == chosenDirection))
            state = 0; // time to enter in North Runner mode
          break;
      }
    }
    this.pickupBaggle();
  }

  private void stepHandOnWall()
  {
    while (!isFacingWall()) {
      stepForward();
      left();
    }
    right();
  }

  Direction chosenDirection = Direction.NORTH;

  private boolean isChosenDirectionFree()
  {
    Direction memorizedD = getDirection();
    this.setDirection(this.chosenDirection);
    boolean isFree = !isFacingWall();
    this.setDirection(memorizedD);
    return isFree;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
