package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaButNfirstEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(butNfirst( param(0).asInstanceOf[Array[Int]].toList, param(1).asInstanceOf[Int] ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def butNfirst(l:List[Int], n:Int): List[Int] = {
	/* BEGIN SOLUTION */
  if (n==0 || l==Nil) l
  else                butNfirst(l.tail, n-1)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
