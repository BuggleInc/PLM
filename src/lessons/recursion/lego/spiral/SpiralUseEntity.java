package lessons.recursion.lego.spiral;

import plm.core.lang.primitives.EntityPrimitives;
import plm.universe.turtles.Turtle;

@EntityPrimitives(SpiralUseEntity.class)
public class SpiralUseEntity extends Turtle {

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void spiral(int steps, int angle, int length, int increment)
  {
    if (steps > 0) {
      forward(length);
      left(angle);
      spiral(steps - 1, angle, length + increment, increment);
    }
    /* BEGIN SOLUTION */
    // Nothing to hide: the template is the solution, but the templating mechanism expects a SOLUTION section
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() { spiral(100, 91, 1, 2); }
  /* END REMOTE */
}
