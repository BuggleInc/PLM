package recursion.cons.nth

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaNthEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( nth(parameter, t.getParameter(1).asInstanceOf[Int]) )
  }

  /* BEGIN TEMPLATE */
  def nth(l:List[Int], n:Int): Int = {
  /* BEGIN SOLUTION */
    if (n == 1) {
      l.head
    } else {
      nth(l.tail, n-1)
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
