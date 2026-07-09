package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaAlarmClockEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(alarmClock(param(0).asInstanceOf[Int], param(1).asInstanceOf[Boolean]) ))
      }
	}

	/* BEGIN TEMPLATE */
	def alarmClock(day:Int, vacation:Boolean): String = {
  	/* BEGIN SOLUTION */
    if (! vacation) {
      if (day >= 1 && day <= 5) {
        return "7:00"
      } else {
        return "10:00"
      }
    } else {
      if (day >= 1 && day <= 5) {
        return "10:00"
      } else {
        return "off"
      }
    }
  	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
