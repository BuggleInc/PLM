package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ButNlastEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(butNlast( param(0).asInstanceOf[Array[Int]].toList, param(1).asInstanceOf[Int] )) )
	  }
	}

	/* BEGIN TEMPLATE */
	def butNlast(l:List[Int], n:Int): List[Int] = {
	/* BEGIN SOLUTION */
  def nFirst(l:List[Int], n:Int): List[Int] = {
    if (n<=0 || l==Nil) return Nil
    return l.head::nFirst(l.tail, n-1)
  }
  return nFirst(l, l.size-n)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
