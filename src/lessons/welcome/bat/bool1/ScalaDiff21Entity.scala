package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaDiff21Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(diff21(param(0).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def diff21(n:Int): Int = {
	/* BEGIN SOLUTION */
  if (n>21)
    return 2*(n-21)
  return 21-n
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
