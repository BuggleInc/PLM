package lessons.lander.lvl1_lander_101;

import lessons.lander.universe.LanderEntity;

public class Lander101Entity extends LanderEntity {
  /* BEGIN REMOTE */
  @Override public void run()
  {
    while (isFlying()) {
      step();
      simulateStep();
    }
  }

  /* BEGIN TEMPLATE */
  @Override public void step()
  {
    /* BEGIN SOLUTION */
    setDesiredThrust(getSpeedY() < -9 ? 4 : 3);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
