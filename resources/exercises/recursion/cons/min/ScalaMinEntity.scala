package recursion.cons.min

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaMinEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( min(parameter) )
  }

  /* BEGIN TEMPLATE */
  def min(l:List[Int]): Int = {
  /* BEGIN SOLUTION */
    min2(l.tail, l.head)
  }

  def min2(l:List[Int], v:Int): Int = {
    if (l==Nil) {
      v
    }
    else if (l.head < v) {
      min2(l.tail, l.head)
    }
    else {
      min2(l.tail, v)
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
