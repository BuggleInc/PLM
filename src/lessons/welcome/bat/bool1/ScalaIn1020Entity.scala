package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIn1020Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(in1020(param(0).asInstanceOf[Int],param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def in1020(a:Int, b:Int):Boolean = {
	/* BEGIN SOLUTION */
   return (a>9 && a<21) || (b>9 && b<21)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
