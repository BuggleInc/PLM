package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaConcatEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(concat( param(0).asInstanceOf[Array[Int]].toList, param(1).asInstanceOf[Array[Int]].toList ) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def concat(l1:List[Int], l2:List[Int]): List[Int] = {
	/* BEGIN SOLUTION */
  def reverse_helper(todo:List[Int], done:List[Int]):List[Int] = {     
	if (todo == Nil) return done
     return reverse_helper(todo.tail, todo.head::done)
  }
  reverse_helper(  reverse_helper(l1, Nil), l2  )
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
