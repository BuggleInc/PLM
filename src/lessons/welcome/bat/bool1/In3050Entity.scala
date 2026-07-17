package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class In3050Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(in3050(param(0).asInstanceOf[Int],param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def in3050(a:Int, b:Int):Boolean = {
	/* BEGIN SOLUTION */
   return (a>29 && a<41 && b>29 && b<41) || (a>39 && a<51 && b>39 && b<51)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
