package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class LengthEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(length( param(0).asInstanceOf[Array[Int]].toList ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def length(l:List[Int]): Int = {
	/* BEGIN SOLUTION */
  l match {
    case a::b => 1+length(b)
    case _    => 0
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
