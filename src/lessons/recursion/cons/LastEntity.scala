package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class LastEntity extends ConsEntity {

    /* BEGIN REMOTE */
    override def run(): Unit = {
      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(last( param(0).asInstanceOf[Array[Int]].toList ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def last(l:List[Int]): Int = {
	/* BEGIN SOLUTION */
	l match {
		case a::Nil => a
		case _::b   => last(b)
		case Nil    => throw new NoSuchElementException("last of an empty list")
	}
	/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
