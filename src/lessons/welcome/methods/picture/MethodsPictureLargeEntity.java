package lessons.welcome.methods.picture;

import java.awt.Color;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsPictureLargeEntity extends SimpleBuggle {

  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    for (int i = 0; i < 9; i++) {
      makeLine(9);
      nextLine();
    }
  }
  void mark()
  {
    brushDown();
    brushUp();
  }

  void makeV(Color c)
  {
    setBrushColor(c);
    stepForward();
    mark();

    stepForward();
    left();
    stepForward();
    mark();

    stepBackward();
    right();
    stepForward();
    mark();

    stepForward();
    left();
  }

  void makePattern()
  {
    makeV(Color.YELLOW);
    makeV(Color.RED);
    makeV(Color.BLUE);
    makeV(Color.GREEN);
    forward(5);
  }

  void makeLine(int count)
  {
    for (int i = 0; i < count; i++)
      makePattern();
    backward(count * 5);
  }

  void nextLine()
  {
    left();
    forward(5);
    right();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
