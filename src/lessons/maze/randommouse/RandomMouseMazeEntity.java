package lessons.maze.randommouse;

public class RandomMouseMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
  public RandomMouseMazeEntity() { forbidTeleportation(); }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    // Your code here =)
    /* BEGIN SOLUTION */
    while (!isOverBaggle()) {
      switch (random3()) {
        case 0:
          if (!isFacingWall()) {
            stepForward();
          }
          break;
        case 1:
          left();
          break;
        case 2:
          right();
          break;
      }
    }
    pickupBaggle();
  }

  public int random3()
  {
    double n = Math.random();
    if (n < 0.33) {
      return 0;
    } else if (n < 0.66) {
      return 1;
    } else {
      return 2;
    }
  }
  /* END SOLUTION */

  /* END TEMPLATE */
  /* END REMOTE */
}
