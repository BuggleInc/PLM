package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaOccurrenceEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(occurences( param(0).asInstanceOf[Array[Int]].toList, param(1).asInstanceOf[Int] ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def occurences(l:List[Int], v:Int): Int = {
	/* BEGIN SOLUTION */
  l match {
    case a::b if a==v => 1 + occurences(b,v)
    case a::b         =>     occurences(b,v)
    case _    => 0
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
