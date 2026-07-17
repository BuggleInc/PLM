package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class LoneTeenEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(loneTeen(param(0).asInstanceOf[Int],param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def loneTeen(a:Int, b:Int):Boolean = {
	/* BEGIN SOLUTION */
	val teenA = a>12 && a<20
	val teenB = b>12 && b<20
	return  (teenA && !teenB) || (teenB && !teenA)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
