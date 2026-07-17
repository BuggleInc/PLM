package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class GreenTicketEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(greenTicket(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int], param(2).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def greenTicket(a:Int, b:Int, c:Int):Int = {
		/* BEGIN SOLUTION */
		if (a == b && b == c)
			return 20
		else if (a == b || b == c || a == c)
			return 10
		else
			return 0
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
