package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaDateFashionEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( dateFashion(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def dateFashion(you:Int, date:Int):Int = {
		/* BEGIN SOLUTION */
		if (you <= 2 || date <= 2) 
			return 0
		else if (you >= 8 || date >= 8)
			return 2
		else
			return 1
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
