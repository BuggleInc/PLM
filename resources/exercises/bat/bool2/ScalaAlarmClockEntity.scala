/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaAlarmClockEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def alarmClock(day: Int, vacation: Boolean): String = {
    /* BEGIN SOLUTION */
    if (!vacation) {
      if (day >= 1 && day <= 5) {
        "7:00"
      } else {
        "10:00"
      }
    } else {
      if (day >= 1 && day <= 5) {
        "10:00"
      } else {
        "off"
      }
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */


  override def run(t: BatTest) {
    t.setResult(alarmClock(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Boolean]))
  }
}
