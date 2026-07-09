package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIcyHotEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(icyHot(param(0).asInstanceOf[Int],param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def icyHot(temp1:Int, temp2:Int):Boolean = {
	/* BEGIN SOLUTION */
   return temp1<0 && temp2>100 || temp1>100 && temp2<0
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
