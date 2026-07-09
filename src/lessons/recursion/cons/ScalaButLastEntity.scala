package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaButLastEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(butLast( param(0).asInstanceOf[Array[Int]].toList )) )
	  }
	}

	/* BEGIN TEMPLATE */
	def butLast(l:List[Int]): List[Int] = {
	/* BEGIN SOLUTION */
  l match {
    case a::b if b==Nil => Nil
    case a::b           => a::butLast(b)
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
