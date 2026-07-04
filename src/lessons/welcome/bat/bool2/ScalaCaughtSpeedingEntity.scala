package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaCaughtSpeedingEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( caughtSpeeding(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Boolean]) );
	}

	/* BEGIN TEMPLATE */
	def caughtSpeeding(speed:Int, isBirthday:Boolean):Int = {
		/* BEGIN SOLUTION */
		if ((isBirthday && speed <= 65) || (speed <= 60))
			return 0
		else if ((isBirthday && speed <= 85) || (speed <= 80))
			return 1
		else
			return 2
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
