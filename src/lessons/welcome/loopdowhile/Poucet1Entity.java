package lessons.welcome.loopdowhile;

import java.awt.Color;
import plm.core.lang.primitives.EntityPrimitives;

@EntityPrimitives(Poucet1Entity.class)
public class Poucet1Entity extends plm.universe.bugglequest.SimpleBuggle implements PoucetEntityPrimitives {
  public Poucet1Entity() { forbidMultiStep(); }

  public boolean crossing() { return getX() % 5 == 1 && getY() % 5 == 1; }

  public boolean exitReached() { return getGroundColor() == Color.orange; }
  /* BINDINGS TRANSLATION */
  boolean sortieTrouvee() { return exitReached(); }
  boolean croisement() { return crossing(); }

  @Override
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    while (!exitReached()) {
      int seen = 0;

      do {
        stepForward();
        if (isOverBaggle())
          seen++;
      } while (!crossing());

      if (seen > 2)
        left();
      else
        right();
    }
    stepForward();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
