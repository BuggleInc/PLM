package lessons.welcome.methods.basics;

import plm.core.model.Game;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouseEntity extends SimpleBuggle {
  @Override public void right() { throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use right() in this exercise. Use left() instead.")); }

  /* BEGIN DEPENDENCY */
  private int line            = -1;
  private boolean studentCode = true;
  /* END DEPENDENCY */
  @Override public void left()
  {
    if (!studentCode) {
      super.left();
      return;
    }

    for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
      if (s.getMethodName().equals("dogHouse")) {
        if (line != -1 && line != s.getLineNumber()) {
          // FIXME: Compute the right line number. Or even better, redo this verication entierely, on the PLM side by inspecting the source code before
          // compilation
          String msg = Game.i18n.tr("Sorry Dave, I cannot let you use left() both in lines {0} and {1} in this "
                                        + "exercise. You can write left() only once in this exercise.",
                                    line, s.getLineNumber());

          throw new RuntimeException(msg);
        } else {
          line = s.getLineNumber();
          super.left();
          return;
        }
      }
    }
  }
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
    studentCode = true;
    brushDown();
    dogHouse();
    brushUp();

    forward(4);

    brushDown();
    dogHouse();
    brushUp();

    forward(2);
    studentCode = false;
    left();
    studentCode = true;
    forward(4);

    brushDown();
    dogHouse();
    brushUp();

    forward(2);
    studentCode = false;
    left();
    studentCode = true;
    forward(4);

    brushDown();
    dogHouse();
  }
}
