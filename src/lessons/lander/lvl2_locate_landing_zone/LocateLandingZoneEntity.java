package lessons.lander.lvl2_locate_landing_zone;

import lessons.lander.universe.LanderEntity;
import plm.universe.Point;

public class LocateLandingZoneEntity extends LanderEntity {
  @Override public void run()
  {
    Point[] landingZone = getLandingZone();
    double targetStart  = landingZone[0].x();
    double targetEnd    = landingZone[1].x();

    while (isFlying()) {
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
      simulateStep();
    }
  }

  /* BEGIN TEMPLATE */
  public Point[] getLandingZone()
  {
    /* return new Segment(new Point(0,0), new Point(0,0)); */
    /* BEGIN SOLUTION */
    Point[] ground = getGround();
    for (int i = 0; i + 1 < ground.length; i++)
      if (ground[i].y() == ground[i + 1].y())
        return new Point[] {ground[i], ground[i + 1]};

    return null;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
