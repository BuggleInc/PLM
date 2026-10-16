package lessons.welcome.loopfor;

import java.awt.Color;
import plm.core.model.Game;
import plm.universe.PrimitiveRefusedException;

public class LoopCourseForestEntity extends plm.universe.bugglequest.SimpleBuggle {
  public LoopCourseForestEntity() { forbidMultiStep(); }

  @Override public void stepBack()
  {
    throw new PrimitiveRefusedException(Game.i18n.tr("Sorry Dave, you cannot run backward like this. Exercising is hard enough -- please don't overplay."));
  }

  Color[] colors = new Color[] {
      new Color(0, 155, 0),   new Color(50, 155, 0),  new Color(100, 155, 0),
      new Color(140, 155, 0), new Color(160, 155, 0), new Color(180, 155, 0),
      new Color(200, 155, 0), new Color(210, 155, 0), Color.red,
  };
  @Override public void step()
  {
    if (!haveSeenError())
      super.step();
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
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    for (int i = 0; i < 7; i++)
      for (int side = 0; side < 4; side++) {
        for (int k = 0; k < 4; k++)
          step();
        left();
        for (int k = 0; k < 2; k++)
          step();
        right();
        for (int k = 0; k < 4; k++)
          step();
        right();
        step();
        step();
        left();
        for (int k = 0; k < 4; k++)
          step();
        left();
      }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
