package recursion.cons.occurrences

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaOccurrenceEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( occurrences(parameter, t.getParameter(1).asInstanceOf[Int]) )
  }

  /* BEGIN TEMPLATE */
  def occurrences(l:List[Int], v:Int): Int = {
  /* BEGIN SOLUTION */
    l match {
      case a::b if a==v => 1 + occurrences(b,v)
      case a::b         =>     occurrences(b,v)
      case _            => 0
	  }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
