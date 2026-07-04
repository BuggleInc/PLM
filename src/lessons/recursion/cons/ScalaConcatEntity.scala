package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaConcatEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( concat( t.getParameter(0).asInstanceOf[Array[Int]].toList, t.getParameter(1).asInstanceOf[Array[Int]].toList ) );
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
