package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ButLastEntity extends ConsEntity {

    /* BEGIN REMOTE */
    override def run(): Unit = {
      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(butLast( param(0).asInstanceOf[Array[Int]].toList ).toArray ) )
	  }
	}

	/* BEGIN TEMPLATE */
	def butLast(l:List[Int]): List[Int] = {
	/* BEGIN SOLUTION */
	l match {
		case Nil | _::Nil => Nil
		case a::b         => a::butLast(b)
	}
	/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
