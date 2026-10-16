package lessons.welcome.methods.slug;

import java.awt.*;

public class SlugTrackingEntity extends plm.universe.bugglequest.SimpleBuggle {

  /* BEGIN REMOTE */
  @Override public void run()
  {
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

  /* BEGIN TEMPLATE */
  boolean isFacingTrail()
  {
    // Write your code here
    /* BEGIN SOLUTION */
    if (isFacingWall())
      return false;
    step();
    boolean res = getGroundColor().equals(Color.green);
    stepBack();
    return res;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
