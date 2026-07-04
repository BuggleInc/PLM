package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaSquirrelPlayEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( squirrelPlay(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Boolean]) );
	}

	/* BEGIN TEMPLATE */
	def squirrelPlay(temp:Int, isSummer:Boolean):Boolean = {
		/* BEGIN SOLUTION */
	   return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90))
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
