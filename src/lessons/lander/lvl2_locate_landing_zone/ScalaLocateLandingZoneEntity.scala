package lessons.lander.lvl2_locate_landing_zone;

import java.util.Iterator;
import scala.collection.JavaConversions._

import lessons.lander.universe._;

class ScalaLocateLandingZoneEntity extends LanderEntity {
  /* BEGIN TEMPLATE */
  def getLandingZone():Segment = {
    /* BEGIN SOLUTION */
    var lastPoint:Point = getGround.get(0);
    for (point <- getGround()) {
      if (point != lastPoint) { // Avoid the loop when point is on the first element
        if (point.y == lastPoint.y)
          return new Segment(lastPoint,point)
      }
      lastPoint = point
    }
    return null;
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  var targetStart = 0.0;
  var targetEnd = 0.0;

  override def initialize():Unit = {
    var landingZone = getLandingZone();
    targetStart = landingZone.start.x;
    targetEnd = landingZone.end.x;
  }

  override def step():Unit = {
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
    
    if (getSpeedY() < -9)
      setDesiredThrust(4)
    else
      setDesiredThrust(3)
  }
}
