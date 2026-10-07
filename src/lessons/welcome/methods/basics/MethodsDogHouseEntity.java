package lessons.welcome.methods.basics;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouseEntity extends SimpleBuggle {
  public MethodsDogHouseEntity() { forbid("right", Game.i18n.tr("Use left() instead.")); }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  void dogHouse()
  {
    /* BEGIN SOLUTION */
    for (int i = 0; i < 4; i++) {
      stepForward();
      stepForward();
      left();
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  @Override public void run()
  {
    brushDown();
    dogHouse();
    brushUp();

    forward(4);

    brushDown();
    dogHouse();
    brushUp();

    forward(2);
    left();
    forward(4);

    brushDown();
    dogHouse();
    brushUp();

    forward(2);
    left();
    forward(4);

    brushDown();
    dogHouse();
  }
  /* END REMOTE */
}
