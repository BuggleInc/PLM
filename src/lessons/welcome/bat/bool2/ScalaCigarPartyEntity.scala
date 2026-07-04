package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaCigarPartyEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( cigarParty(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Boolean]) );
	}

	/* BEGIN TEMPLATE */
	def cigarParty(cigars:Int, isWeekend:Boolean):Boolean = {
	/* BEGIN SOLUTION */
   return (isWeekend && cigars >= 40) || (! isWeekend && (cigars >= 40) && (cigars <= 60))
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
