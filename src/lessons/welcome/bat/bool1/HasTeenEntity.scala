package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class HasTeenEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(hasTeen(param(0).asInstanceOf[Int],param(1).asInstanceOf[Int],param(2).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def hasTeen(a:Int, b:Int, c:Int): Boolean = {
	/* BEGIN SOLUTION */
   return (a>12 && a<20) || (b>12 && b<20) || (c>12 && c<20)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
