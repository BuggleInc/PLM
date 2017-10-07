package recursion.cons.alldifferent

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaButLastEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( butLast(parameter) )
  }

  /* BEGIN TEMPLATE */
  def butLast(l:List[Int]): List[Int] = {
  /* BEGIN SOLUTION */
    l match {
      case a::Nil => Nil
      case a::b   => a::butLast(b)
      case _ => Nil
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
