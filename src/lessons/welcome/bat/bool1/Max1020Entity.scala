package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class Max1020Entity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(max1020(param(0).asInstanceOf[Int], param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def max1020(a:Int, b:Int):Int = {
	/* BEGIN SOLUTION */
	val A = Math.max(a,b)
	val B = Math.min(a,b)
	if (A<21 && A>9)
		return A
	if (B<21 && B>9)
		return B
	return 0
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
