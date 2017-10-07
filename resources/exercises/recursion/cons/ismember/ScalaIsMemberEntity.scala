package recursion.cons.ismember

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaIsMemberEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( isMember(parameter, t.getParameter(1).asInstanceOf[Int]) )
  }

  /* BEGIN TEMPLATE */
  def isMember(l:List[Int], v:Int): Boolean = {
  /* BEGIN SOLUTION */
    l match {
      case a::b if a==v => true
      case a::b         => isMember(b,v)
			case _    => false
	  }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
