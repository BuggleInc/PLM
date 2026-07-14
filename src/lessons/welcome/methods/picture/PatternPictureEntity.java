package lessons.welcome.methods.picture;

import java.awt.Color;
import plm.universe.bugglequest.SimpleBuggle;

public class PatternPictureEntity extends SimpleBuggle {

  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    bigSquare();
    forward(4);
    bigSquare();

    backward(4);
    left();
    forward(4);
    right();

    bigSquare();
    forward(4);
    bigSquare();
  }
  void mark()
  {
    brushDown();
    brushUp();
  }

  void squareA(Color c)
  {
    setBrushColor(c);

    stepForward();
    mark();

    left();
    stepForward();

    left();
    stepForward();
    mark();

    left();
    stepForward();
    left();
  }

  void squareB(Color c)
  {
    setBrushColor(c);
    mark();

    stepForward();

    left();
    stepForward();
    mark();

    left();
    stepForward();

    left();
    stepForward();
    left();
  }

  void bigSquare()
  {
    squareA(Color.RED);
    forward(2);
    squareB(Color.BLUE);
    backward(2);
    left();
    forward(2);
    right();
    squareB(Color.YELLOW);
    forward(2);
    squareA(Color.GREEN);

    backward(2);
    left();
    backward(2);
    right();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
