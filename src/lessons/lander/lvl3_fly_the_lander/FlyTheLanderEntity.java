package lessons.lander.lvl3_fly_the_lander;

import lessons.lander.universe.LanderEntity;
import plm.universe.Point;

public class FlyTheLanderEntity extends LanderEntity {
  @Override public void run()
  {
    initialize();
    while (isFlying()) {
      step();
      simulateStep();
    }
  }

  /* BEGIN TEMPLATE */
  /* BEGIN HIDDEN */
  double targetStart;
  double targetEnd;
  /* END HIDDEN */
  public void initialize()
  {
    /* BEGIN HIDDEN */
    Point[] ground  = getGround();
    Point lastPoint = ground[0];
    for (Point point : ground) {
      if (point != lastPoint && lastPoint.y() == point.y()) {
        targetStart = lastPoint.x();
        targetEnd   = point.x();
        return;
      }
      lastPoint = point;
    }
    /* END HIDDEN */
  }

  public void step()
  {
    /* BEGIN SOLUTION */
    if (getX() < targetStart) {
      setDesiredAngle(-30);
    } else if (getX() > targetEnd) {
      setDesiredAngle(30);
    } else {
      if (getSpeedX() > 5) {
        setDesiredAngle(25);
      } else if (getSpeedX() < -5) {
        setDesiredAngle(-25);
      } else {
        setDesiredAngle(0);
      }
    }
    setDesiredThrust(getSpeedY() < -9 ? 4 : 3);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
