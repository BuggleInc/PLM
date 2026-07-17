package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class InOrderEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(inOrder(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int], param(2).asInstanceOf[Int], param(3).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def inOrder(a:Int, b:Int, c:Int, bOk:Boolean):Boolean = {
	/* BEGIN SOLUTION */
		return (bOk || (b > a)) && (c > b)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
