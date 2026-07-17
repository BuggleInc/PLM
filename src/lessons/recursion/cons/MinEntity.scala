package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class MinEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(min( param(0).asInstanceOf[Array[Int]].toList ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def min(l:List[Int]): Int = {
	/* BEGIN SOLUTION */
  def min2(l:List[Int], v:Int): Int = {
    if (l==Nil) return v
    if (l.head < v) return min2(l.tail, l.head)
    return min2(l.tail, v)
  }
  return min2(l.tail, l.head)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
