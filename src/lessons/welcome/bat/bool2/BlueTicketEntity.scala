package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class BlueTicketEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(blueTicket(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int], param(2).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def blueTicket(a:Int, b:Int, c:Int):Int = {
		/* BEGIN SOLUTION */
		val ab = a + b
		val ac = a + c
		val bc = b + c
		if (ab == 10 || ac == 10 || bc == 10)
			return 10
		else if (ab == (bc + 10) || ab == (ac + 10))
			return 5
		else
			return 0
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
