package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaCaughtSpeedingEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def caughtSpeeding(speed: Int, isBirthday: Boolean): Int = {
    /* BEGIN SOLUTION */
    if ((isBirthday && speed <= 65) || (speed <= 60))
      0
    else if ((isBirthday && speed <= 85) || (speed <= 80))
      1
    else
      2
    /* END SOLUTION */
  }
  /* END TEMPLATE */


  override def run(t: BatTest) {
    t.setResult(caughtSpeeding(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Boolean]) )
  }
}
