package lessons.welcome.methods.slug;

import java.awt.*;

public class SlugHuntingEntity extends plm.universe.bugglequest.SimpleBuggle {

  /* BEGIN REMOTE */
  @Override public void run() { hunt(); }

  /* BEGIN TEMPLATE */
  public void hunt()
  {
    // Write your code here
    /* BEGIN SOLUTION */
    while (!isOverBaggle()) {
      if (isFacingTrail()) {
        brushDown();
        step();
        brushUp();
      } else {
        left();
      }
    }
    pickupBaggle();
  }

  boolean isFacingTrail()
  {
    if (isFacingWall())
      return false;

    step();
    boolean res = getGroundColor().equals(Color.green);
    stepBack();
    return res;

    /* END SOLUTION */
  }
  // Copy your isFacingTrail here
  /* END TEMPLATE */
  /* END REMOTE */
}
