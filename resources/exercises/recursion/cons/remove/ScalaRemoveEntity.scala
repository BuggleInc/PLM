package recursion.cons.remove

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaRemoveEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( remove(parameter, t.getParameter(1).asInstanceOf[Int]) )
  }

  /* BEGIN TEMPLATE */
  def remove(l:List[Int], v:Int): List[Int] = {
  /* BEGIN SOLUTION */
    l match {
      case a::_ if a==v => remove(l.tail, v)
      case a::b         => a::remove(b, v)
      case _            => Nil
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
