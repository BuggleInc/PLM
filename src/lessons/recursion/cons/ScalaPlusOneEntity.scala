package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaPlusOneEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(plusOne(param(0).asInstanceOf[Array[Int]].toList) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def plusOne(l:List[Int]): List[Int] = {
	/* BEGIN SOLUTION */
  l match {
    case a::b => (a+1)::plusOne(b)
    case _    => Nil
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
