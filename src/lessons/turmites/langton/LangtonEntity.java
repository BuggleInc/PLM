package lessons.turmites.langton;

import java.awt.Color;
import lessons.turmites.universe.TurmiteEntity;

public class LangtonEntity extends TurmiteEntity {
  /* BEGIN TEMPLATE */
  public void step()
  {
    /* BEGIN SOLUTION */
    if (getGroundColor() == Color.white) {
      right();

      setBrushColor(Color.black);
      brushDown();
      brushUp();

      stepForward();
    } else {
      left();

      setBrushColor(Color.white);
      brushDown();
      brushUp();

      stepForward();
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run()
  {
    int nbSteps = getParamInt(0);
    for (int i = 0; i < nbSteps; i++) {
      step();
      stepDone();
    }
  }
}
