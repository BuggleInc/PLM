package lessons.welcome.summative;

import plm.core.model.Game;
import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

public class MoriaEntity extends SimpleBuggle {
  @Override public void forward(int i)
  {
    throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
  }

  @Override public void backward(int i)
  {
    throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
  }

  @Override
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
}
