package lessons.welcome.methods.slug;

import java.awt.Color;

public class SlugTrackingEntity extends plm.universe.bugglequest.SimpleBuggle {

  @Override public void run()
  {
    while (!isOverBaggle()) {
      if (isFacingTrail()) {
        brushDown();
        stepForward();
        brushUp();
      } else {
        left();
      }
    }
    pickupBaggle();
  }

  /* BEGIN TEMPLATE */
  boolean isFacingTrail()
  {
    // Write your code here
    /* BEGIN SOLUTION */
    if (isFacingWall())
      return false;
    stepForward();
    boolean res = getGroundColor() == Color.green;
    stepBackward();
    return res;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
