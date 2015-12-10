package lessons.lander.lvl3_fly_the_lander;

import java.util.Iterator;
import scala.collection.JavaConversions._

import lessons.lander.universe._;

class ScalaFlyTheLanderEntity extends LanderEntity {
  /* BEGIN TEMPLATE */
  /* BEGIN HIDDEN */
  var targetStart=0.0
  var targetEnd=0.0
  /* END HIDDEN */
  override def initialize():Unit = {
    /* BEGIN HIDDEN */
    var lastPoint:Point = getGround.get(0);
    for (point <- getGround()) {
      if (point != lastPoint) { // Avoid the loop when point is on the first element
        if (point.y == lastPoint.y) {
          targetStart = lastPoint.x
          targetEnd = point.x
        }
      }
      lastPoint = point
    }
    /* END HIDDEN */
  }

  override def step():Unit = {
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
    
    if (getSpeedY() < -9) {
    	setDesiredThrust(4)
    } else {
    	setDesiredThrust(3);
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
