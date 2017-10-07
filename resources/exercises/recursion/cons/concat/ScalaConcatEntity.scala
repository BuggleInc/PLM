package recursion.cons.concat

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaConcatEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter1: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])
    val parameter2: List[Int] = RecList.fromArraytoScalaList(t.getParameter(1).asInstanceOf[Array[Int]])  
    t.setResult( concat(parameter1, parameter2) )
  }

  /* BEGIN TEMPLATE */
  def concat(l1:List[Int], l2:List[Int]): List[Int] = {
  /* BEGIN SOLUTION */
    reverse_helper(  reverse_helper(l1, Nil), l2  )
  }
  def reverse_helper(todo:List[Int], done:List[Int]):List[Int] = {
    if (todo == Nil) {
      done
    } else {
      reverse_helper(todo.tail, todo.head::done)
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
