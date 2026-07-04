package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaGreenTicketEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( greenTicket(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int], t.getParameter(2).asInstanceOf[Int]) );
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
