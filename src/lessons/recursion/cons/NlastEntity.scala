package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class NlastEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
		setTestResult(i, serialize(nlast( param(0).asInstanceOf[Array[Int]].toList, param(1).asInstanceOf[Int] ).toArray ))
	  }
	}

	/* BEGIN TEMPLATE */
	def nlast(l:List[Int], n:Int): List[Int] = {
	/* BEGIN SOLUTION */
  def ButnFirst(l:List[Int], n:Int): List[Int] = {
    if (n<=0 || l==Nil) return l
    return ButnFirst(l.tail, n-1)
  }
  return ButnFirst(l, l.size-n)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
