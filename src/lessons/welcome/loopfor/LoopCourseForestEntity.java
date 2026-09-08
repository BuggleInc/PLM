package lessons.welcome.loopfor;

import java.awt.Color;
import plm.core.model.Game;

public class LoopCourseForestEntity extends plm.universe.bugglequest.SimpleBuggle {
  @Override public void forward(int i)
  {
    throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
  }
  @Override public void backward(int i)
  {
    throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
  }
  @Override public void stepBackward()
  {
    throw new RuntimeException(Game.i18n.tr("Sorry Dave, you cannot run backward like this. Exercising is hard enough -- please don't overplay."));
  }

  Color[] colors = new Color[] {
      new Color(0, 155, 0),   new Color(50, 155, 0),  new Color(100, 155, 0),
      new Color(140, 155, 0), new Color(160, 155, 0), new Color(180, 155, 0),
      new Color(200, 155, 0), new Color(210, 155, 0), Color.red,
  };
  @Override public void stepForward()
  {
    if (!haveSeenError())
      super.stepForward();
    Color c = getGroundColor();
    if (c == Color.blue) {
      if (!haveSeenError())
        seenError(Game.i18n.tr("You fall into water."));
    } else {
      for (int i = 0; i < colors.length - 1; i++)
        if (colors[i].equals(c)) {
          if (i == colors.length - 1)
            c = colors[i];
          else
            c = colors[i + 1];
          break;
        }
      setBrushColor(c);
      brushDown();
      brushUp();
    }
  }

  @Override
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    for (int i = 0; i < 7; i++)
      for (int side = 0; side < 4; side++) {
        for (int step = 0; step < 4; step++)
          stepForward();
        left();
        for (int step = 0; step < 2; step++)
          stepForward();
        right();
        for (int step = 0; step < 4; step++)
          stepForward();
        right();
        stepForward();
        stepForward();
        left();
        for (int step = 0; step < 4; step++)
          stepForward();
        left();
      }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
