package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class RedTicketEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(redTicket(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int], param(2).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def redTicket(a:Int, b:Int, c:Int):Int = {
		/* BEGIN SOLUTION */
		if (a == b && b == c && c == 2)
			return 10
		else if (a == b && b == c)
			return 5
		else if (b != a && c != a)
			return 1
		else
			return 0
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
