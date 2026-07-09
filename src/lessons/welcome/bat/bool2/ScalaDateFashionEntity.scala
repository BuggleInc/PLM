package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaDateFashionEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(dateFashion(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int]) ))
	  }
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
