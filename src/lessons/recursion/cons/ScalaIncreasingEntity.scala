package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity
import lessons.recursion.cons.universe.RecList

class ScalaIncreasingEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(increasing( param(0).asInstanceOf[Array[Int]].toList ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def increasing(l:List[Int]): Boolean = {
	/* BEGIN SOLUTION */
  if (l == Nil || l.tail == Nil) return true
  if (l.head > l.tail.head)      return false
  return increasing(l.tail)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
