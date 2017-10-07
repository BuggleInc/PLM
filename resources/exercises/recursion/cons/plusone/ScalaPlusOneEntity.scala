package recursion.cons.plusone

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaPlusOneEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( plusOne(parameter) )
  }

  /* BEGIN TEMPLATE */
  def plusOne(l:List[Int]): List[Int] = {
  /* BEGIN SOLUTION */
    l match {
      case a::b => (a+1)::plusOne(b)
      case _    => Nil
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
