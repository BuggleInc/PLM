package lessons.lander.level1;

import lessons.lander.universe.LanderEntity;

public class Level1LanderEntity extends LanderEntity {
  @Override
  public void step() {
    /* BEGIN TEMPLATE */
    /* BEGIN SOLUTION */
    setDesiredThrust(getSpeedY() < -9 ? 4 : 3);
    /* END SOLUTION */
    /* END TEMPLATE */
  }
}
