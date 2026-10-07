package lessons.maze.pledge;

import plm.universe.Direction;

public class PledgeMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
  public PledgeMazeEntity() { forbidTeleportation(); }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    int state     = 0;
    this.angleSum = 0;
    this.setDirection(this.chosenDirection);
    while (!isOverBaggle()) {
      switch (state) {
        case 0: // North runner mode
          while (!isFacingWall())
            stepForward();

          right(); // make sure that we have a left wall
          angleSum--;
          state = 1; // time to enter the Left Follower mode
          break;
        case 1:                  // Left Follower Mode
          this.stepHandOnWall(); // follow the left wall
          if (this.isChosenDirectionFree() && this.angleSum == 0)
            state = 0; // time to enter in North Runner mode

          break;
      }
    }
    pickupBaggle();
  }

  int angleSum;

  private void stepHandOnWall()
  {
    while (!isFacingWall()) {
      stepForward();
      left();
      this.angleSum++;
    }
    right();
    this.angleSum--;
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
