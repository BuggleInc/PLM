package lessons.welcome.methods.args;

import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsArgsEntity extends SimpleBuggle {
  public MethodsArgsEntity() { forbidMultiStep(); }

  /* BEGIN REMOTE */
  @Override public void run() { move(getY(), getDirection() == Direction.NORTH); }

  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  public void move(int nbPas, boolean forward)
  {
    if (forward) {
      for (int i = 0; i < nbPas; i++)
        stepForward();
    } else {
      for (int i = 0; i < nbPas; i++)
        stepBackward();
    }
  }
  /* END SOLUTION */
  /* END TEMPLATE */
  /* END REMOTE */
}
