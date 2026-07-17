package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class RemoveEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(remove(param(0).asInstanceOf[Array[Int]].toList, param(1).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def remove(l:List[Int], v:Int): List[Int] = {
	/* BEGIN SOLUTION */
  l match {
    case a::_ if a==v => remove(l.tail, v)
    case a::b         => a::remove(b, v)
    case _            => Nil
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
