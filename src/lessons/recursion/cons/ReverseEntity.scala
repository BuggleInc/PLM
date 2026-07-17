package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ReverseEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(reverse(param(0).asInstanceOf[Array[Int]].toList).toArray ))
	  }
	}

	/* BEGIN TEMPLATE */
	def reverse(l:List[Int]): List[Int] = {
	/* BEGIN SOLUTION */
  def lambda(l:List[Int], tmp:List[Int]):List[Int] = {     
	if (l == Nil) return tmp
     return lambda(l.tail, l.head::tmp)
  }
  lambda(l, Nil)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
