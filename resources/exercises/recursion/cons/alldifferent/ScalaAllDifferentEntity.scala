package recursion.cons.alldifferent

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaAllDifferentEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( allDifferent(parameter) )
  }

  /* BEGIN TEMPLATE */
  def allDifferent(l:List[Int]): Boolean = {
  /* BEGIN SOLUTION */
    if (l == Nil) {
    	return true
    }
    if (isMember(l.tail, l.head)) {
      return false
    }
    return allDifferent(l.tail)
	}

  def isMember(l:List[Int], v:Int): Boolean = {
    if (l == Nil) {
      return false
    }
    if (v == l.head) {
      return true
    }
    return isMember(l.tail, v)
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
