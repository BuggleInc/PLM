package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaCigarPartyEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(cigarParty(param(0).asInstanceOf[Int], param(1).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def cigarParty(cigars:Int, isWeekend:Boolean):Boolean = {
	/* BEGIN SOLUTION */
   return (isWeekend && cigars >= 40) || (! isWeekend && (cigars >= 40) && (cigars <= 60))
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
