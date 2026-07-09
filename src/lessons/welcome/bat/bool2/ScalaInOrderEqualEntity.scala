package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaInOrderEqualEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(inOrderEqual(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int], param(2).asInstanceOf[Int], param(3).asInstanceOf[Boolean]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def inOrderEqual(a:Int, b:Int, c:Int, equalOk:Boolean):Boolean = {
		/* BEGIN SOLUTION */
			return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c)
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
