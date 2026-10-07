package lessons.welcome.array.basics;

import java.awt.Color;
import plm.core.model.Game;

public class Array1Entity extends plm.universe.bugglequest.SimpleBuggle {
  public Array1Entity()
  {
    forbid("setX", Game.i18n.tr("Walk to your goal instead."));
    forbid("setY", Game.i18n.tr("Walk to your goal instead."));
    forbid("setPos", Game.i18n.tr("Walk to your goal instead."));
  }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    Color[] colors = new Color[getWorldHeight()];

    /* read the colors */
    for (int i = 0; i < getWorldHeight(); i++) {
      colors[i] = getGroundColor();
      stepForward();
    }

    /* duplicate the pattern */
    for (int i = 1; i < getWorldWidth(); i++) {
      left();
      stepForward();
      right();
      stepForward();
      makeLine(colors);
    }
  }
  void makeLine(Color[] colors)
  {
    for (int i = 0; i < getWorldWidth(); i++) {
      mark(colors[i]);
      stepForward();
    }
  }
  void mark(Color c)
  {
    setBrushColor(c);
    brushDown();
    brushUp();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
