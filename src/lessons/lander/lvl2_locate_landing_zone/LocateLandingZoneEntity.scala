package lessons.lander.lvl2_locate_landing_zone;

import lessons.lander.universe._;
import lessons.lander.universe.LanderWorld._;

class LocateLandingZoneEntity extends LanderEntity {
  /* BEGIN REMOTE */
  override def run(): Unit = {
    val landingZone = getLandingZone()
    val targetStart = landingZone(0).x
    val targetEnd   = landingZone(1).x

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
      setDesiredThrust(if (getSpeedY() < -9) 4 else 3)
      simulateStep()
    }
  }

  /* BEGIN TEMPLATE */
  def getLandingZone(): Array[Point] = {
    /* return Array(new Point(0,0), new Point(0,0)) */
    /* BEGIN SOLUTION */
    getGround().sliding(2).find(pair => pair(0).y == pair(1).y).orNull
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
