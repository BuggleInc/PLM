package lessons.welcome.summative;

import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

public class MoriaEntity extends SimpleBuggle {
  public MoriaEntity() { forbidMultiStep(); }

  @Override
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    @SuppressWarnings("unused") Direction d = Direction.NORTH; // Some people want to use Direction in that lesson

    back();
    while (!isFacingWall()) {
      while (!isOverBaggle() && !isFacingWall())
        stepForward();
      if (isOverBaggle()) {
        pickupBaggle();
        back();
        while (!isOverBaggle())
          stepForward();
        stepBackward();
        dropBaggle();
        back();
        stepForward();
      }
    }
    right();
    stepForward();
    left();
    stepForward();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
