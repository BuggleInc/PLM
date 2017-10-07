package recursion.cons.length

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaLengthEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( length(parameter) )
  }

  /* BEGIN TEMPLATE */
  def length(l:List[Int]): Int = {
  /* BEGIN SOLUTION */
    l match {
    	case a::b => 1+length(b)
      case _    => 0
    }
	/* END SOLUTION */
  }
  /* END TEMPLATE */
}
