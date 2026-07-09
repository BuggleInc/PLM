package lessons.welcome.bat.bool2

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaTwoAsOneEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(twoAsOne(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int], param(2).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def twoAsOne(a:Int, b:Int, c:Int):Boolean = {
	/* BEGIN SOLUTION */
   return (a + b == c) || (a + c == b) || (b + c == a)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
