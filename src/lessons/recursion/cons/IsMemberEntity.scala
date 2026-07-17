package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class IsMemberEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(isMember( param(0).asInstanceOf[Array[Int]].toList, param(1).asInstanceOf[Int] ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def isMember(l:List[Int], v:Int): Boolean = {
	/* BEGIN SOLUTION */
  l match {
    case a::b if a==v => true
    case a::b         => isMember(b,v)
    case _    => false
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
