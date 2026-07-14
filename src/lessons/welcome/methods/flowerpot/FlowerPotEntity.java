package lessons.welcome.methods.flowerpot;

import java.awt.Color;
import plm.universe.bugglequest.SimpleBuggle;
public class FlowerPotEntity extends SimpleBuggle {

  public void run() { growFlowers(); }

  /* BEGIN TEMPLATE */
  void growFlowers()
  {
    /* BEGIN SOLUTION */
    line(Color.RED, Color.CYAN);

    right();
    forward(2);
    left();
    halfLine(Color.ORANGE);
    right();
    forward(2);
    left();

    line(Color.PINK, Color.GREEN);
  }

  void makeFlower(Color c)
  {
    setBrushColor(c);
    brushDown();
    forward(2);
    stepBackward();
    left();
    stepForward();
    backward(2);
    stepForward();
    setBrushColor(Color.YELLOW);
    brushUp();
    right();
  }

  void line(Color c1, Color c2)
  {
    makeFlower(c1);
    forward(3);
    makeFlower(c2);
    backward(5);
  }
  void halfLine(Color c)
  {
    forward(2);
    makeFlower(c);
    backward(3);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
