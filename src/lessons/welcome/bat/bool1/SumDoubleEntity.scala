package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class SumDoubleEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(sumDouble( param(0).asInstanceOf[Int], param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def sumDouble(a: Integer, b: Integer): Integer = {
		/* BEGIN SOLUTION */
	  if (a==b) {
	    return (a+b)*2
	  }
	  return a+b
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
