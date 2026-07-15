package lessons.welcome.methods.slug;

import java.awt.*;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;

@EntityPrimitives(lessons.welcome.methods.slug.SlugSnailEntity.class)
public class SlugSnailEntity extends plm.universe.bugglequest.SimpleBuggle {

  @Primitive(215) public Color getColorIntParam() { return (Color)getParam(0); }

  @Override public void run() { hunt(getColorIntParam()); }

  /* BEGIN TEMPLATE */
  public void hunt(Color c)
  {
    // Write your code here
    /* BEGIN SOLUTION */
    while (!isOverBaggle()) {
      if (isFacingTrail(c)) {
        brushDown();
        stepForward();
        brushUp();
      } else {
        left();
      }
    }
    pickupBaggle();
    /* END SOLUTION */
  }

  // here comes your isFacingTrail method

  /* BEGIN HIDDEN */
  boolean isFacingTrail(Color c)
  {
    if (isFacingWall())
      return false;

    stepForward();
    boolean res = getGroundColor().equals(c);
    stepBackward();
    return res;
  }
  /* END HIDDEN */
  /* END TEMPLATE */
}
