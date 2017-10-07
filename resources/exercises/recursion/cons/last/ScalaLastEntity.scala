package recursion.cons.last

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaLastEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( last(parameter) )
  }

  /* BEGIN TEMPLATE */
  def last(l:List[Int]): Int = {
  /* BEGIN SOLUTION */
    l match {
      case a::b if b==Nil => a
      case a::b            => last(b)
	  }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
