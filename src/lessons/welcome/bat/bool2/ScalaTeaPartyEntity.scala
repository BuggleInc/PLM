package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaTeaPartyEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(teaParty(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def teaParty(tea:Int, candy:Int): Int = {
		/* BEGIN SOLUTION */
		if (tea < 5 || candy < 5)
			return 0
		else if (tea >= 2*candy || candy >= 2*tea)
			return 2
		else
			return 1
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
