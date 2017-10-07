package recursion.cons.nfirst

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaNfirstEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( nfirst(parameter, t.getParameter(1).asInstanceOf[Int]) )
  }

  /* BEGIN TEMPLATE */
  def nfirst(l:List[Int], n:Int): List[Int] = {
  /* BEGIN SOLUTION */
    if (n == 0) {
      Nil
    } else {
      l.head :: nfirst(l.tail, n-1)
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
