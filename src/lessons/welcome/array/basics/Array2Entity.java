package lessons.welcome.array.basics;

import java.awt.Color;
import plm.universe.bugglequest.SimpleBuggle;

public class Array2Entity extends SimpleBuggle {
  public Array2Entity() { forbidTeleportation(); }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  void mark(Color c)
  {
    setBrushColor(c);
    brushDown();
    brushUp();
  }

  public void run()
  {
    Color[] colors = new Color[getWorldHeight()];

    /* read the colors */
    colors[0] = getGroundColor();
    for (int i = 1; i < getWorldHeight(); i++) {
      stepForward();
      colors[i] = getGroundColor();
    }
    backward(getWorldHeight() - 1);

    /* Duplicate the pattern */
    for (int i = 1; i < getWorldWidth(); i++) {
      left();
      stepForward();
      right();
      makeLine(colors);
    }
  }

  void makeLine(Color[] colors)
  {
    int offset = Integer.parseInt(readMessage());
    mark(colors[(0 + offset) % colors.length]);
    for (int i = 1; i < getWorldWidth(); i++) {
      stepForward();
      mark(colors[(i + offset) % colors.length]);
    }
    backward(getWorldHeight() - 1);
  }
  /* END SOLUTION */
  /* END TEMPLATE */
  /* END REMOTE */
}
