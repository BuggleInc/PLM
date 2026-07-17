package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class NfirstEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(nfirst( param(0).asInstanceOf[Array[Int]].toList, param(1).asInstanceOf[Int] ).toArray ))
	  }
	}

	/* BEGIN TEMPLATE */
	def nfirst(l:List[Int], n:Int): List[Int] = {
	/* BEGIN SOLUTION */
  if (n == 0) return Nil
  else        return l.head :: nfirst(l.tail, n-1)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
