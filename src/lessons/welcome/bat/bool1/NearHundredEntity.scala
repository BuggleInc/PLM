package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class NearHundredEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(nearHundred(param(0).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def nearHundred(n:Int): Boolean = {
	/* BEGIN SOLUTION */
  return (90<=n && n<=110)||(190<=n&&n<=210);
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
