package lessons.lander.full;

import java.util.Iterator;

import lessons.lander.universe.LanderEntity;
import lessons.lander.universe.Point;

public class FullLanderEntity extends LanderEntity {
  /* BEGIN TEMPLATE */
  /* BEGIN HIDDEN */
  double targetStart;
  double targetEnd;
  /* END HIDDEN */
  /* BEGIN HIDDEN */
  @Override
  /* END HIDDEN */
  public void init() {
    /* BEGIN HIDDEN */
    Iterator<Point> ground = getGround().iterator();
    Point lastPoint = ground.next();
    while (ground.hasNext()) {
      Point point = ground.next();
      if (lastPoint.y() == point.y()) {
        targetStart = lastPoint.x();
        targetEnd = point.x();
        return;
      }
      lastPoint = point;
    }
    /* END HIDDEN */
  }

  /* BEGIN HIDDEN */
  @Override
  /* END HIDDEN */
  public void step() {
    /* BEGIN SOLUTION */
    if (getX() < targetStart) {
      setDesiredAngle(-30);
    } else if (getX() > targetEnd) {
      setDesiredAngle(30);
    } else {
      if (getSpeedX() > 5) {
        setDesiredAngle(20);
      } else if (getSpeedX() < -5) {
        setDesiredAngle(-20);
      } else {
        setDesiredAngle(0);
      }
    }
    setDesiredThrust(getSpeedY() < -5 ? 4 : 3);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
