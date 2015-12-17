package lessons.lander.lvl2_locate_landing_zone;

import java.util.Iterator;

import plm.universe.lander.LanderEntity;
import plm.universe.lander.Point;
import plm.universe.lander.Segment;

public class LocateLandingZoneEntity extends LanderEntity {
  /* BEGIN TEMPLATE */
  public Segment getLandingZone() {
    /* return new Segment(new Point(0,0), new Point(0,0)); */
    /* BEGIN SOLUTION */
    Iterator<Point> ground = getGround().iterator();
    Point lastPoint = ground.next();
    while (ground.hasNext()) {
      Point point = ground.next();
      if (lastPoint.y() == point.y()) {
        return new Segment(lastPoint, point);
      }
      lastPoint = point;
    }
    return null;
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  /* BEGIN HIDDEN */
  double targetStart = 0;
  double targetEnd = 0;

  @Override
  public void initialize() {
    Segment landingZone = getLandingZone();
    targetStart = landingZone.start().x();
    targetEnd = landingZone.end().x();
  }

  @Override
  public void step() {
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
  }
  /* END HIDDEN */
}
