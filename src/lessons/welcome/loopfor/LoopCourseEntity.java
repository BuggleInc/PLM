package lessons.welcome.loopfor;

import java.awt.Color;
import plm.core.model.Game;
import plm.universe.PrimitiveRefusedException;

public class LoopCourseEntity extends plm.universe.bugglequest.SimpleBuggle {
  public LoopCourseEntity() { forbidMultiStep(); }

  @Override public void stepBack()
  {
    throw new PrimitiveRefusedException(Game.i18n.tr("Sorry Dave, you cannot run backward like this. Exercising is hard enough -- please don't overplay."));
  }

  Color[] colors = new Color[] {Color.white,
                                new Color(255, 240, 240),
                                new Color(255, 220, 220),
                                new Color(255, 205, 205),
                                new Color(255, 190, 190),
                                new Color(255, 170, 170),
                                new Color(255, 150, 150),
                                new Color(255, 130, 130),
                                new Color(255, 110, 110),
                                new Color(255, 45, 45),
                                new Color(255, 5, 5),
                                Color.magenta};
  @Override public void step()
  {
    super.step();
    Color c = getGroundColor();
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

  @Override
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    for (int i = 0; i < 10; i++)
      for (int side = 0; side < 4; side++) {
        for (int k = 0; k < 8; k++)
          step();
        left();
      }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
