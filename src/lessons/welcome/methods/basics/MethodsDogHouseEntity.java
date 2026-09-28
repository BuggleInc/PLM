package lessons.welcome.methods.basics;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouseEntity extends SimpleBuggle {
  @Override public void right() { throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use right() in this exercise. Use left() instead.")); }

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
