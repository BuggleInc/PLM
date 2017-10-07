package recursion.cons.increasing

import plm.universe.bat.BatTest
import plm.universe.cons.ConsEntity
import plm.universe.cons.RecList

class ScalaIncreasingEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( increasing(parameter) )
  }

  /* BEGIN TEMPLATE */
  def increasing(l:List[Int]): Boolean = {
  /* BEGIN SOLUTION */
    if (l == Nil || l.tail == Nil) {
      true
    }
    else if (l.head > l.tail.head) {
      false
    }
    else {
      increasing(l.tail)
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
