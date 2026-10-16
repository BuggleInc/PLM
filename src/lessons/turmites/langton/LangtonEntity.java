package lessons.turmites.langton;

import java.awt.Color;
import lessons.turmites.universe.TurmiteEntity;

public class LangtonEntity extends TurmiteEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void tick()
  {
    /* BEGIN SOLUTION */
    if (getGroundColor() == Color.white) {
      right();

      setBrushColor(Color.black);
      brushDown();
      brushUp();

      step();
    } else {
      left();

      setBrushColor(Color.white);
      brushDown();
      brushUp();

      step();
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run()
  {
    int nbSteps = getParamInt(0);
    for (int i = 0; i < nbSteps; i++) {
      tick();
      stepDone();
    }
  }
  /* END REMOTE */
}
