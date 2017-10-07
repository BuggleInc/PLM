package recursion.cons.reverse

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaReverseEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( reverse(parameter) )
  }

  /* BEGIN TEMPLATE */
  def reverse(l:List[Int]): List[Int] = {
  /* BEGIN SOLUTION */
    lambda(l, Nil)
  }
  def lambda(l:List[Int], tmp:List[Int]):List[Int] = {
    if (l == Nil) {
      tmp
    } else {
      lambda(l.tail, l.head::tmp)
    }
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
