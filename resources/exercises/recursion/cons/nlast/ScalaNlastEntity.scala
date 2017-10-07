package recursion.cons.nlast

import plm.universe.bat.BatTest
import plm.universe.cons.RecList
import plm.universe.cons.ConsEntity

class ScalaNlastEntity extends ConsEntity {

  override def run(t: BatTest) {
    val parameter: List[Int] = RecList.fromArraytoScalaList(t.getParameter(0).asInstanceOf[Array[Int]])   
    t.setResult( nlast(parameter, t.getParameter(1).asInstanceOf[Int]) )
  }

  /* BEGIN TEMPLATE */
  def nlast(l:List[Int], n:Int): List[Int] = {
  /* BEGIN SOLUTION */
    def nfirst(lst:List[Int], n:Int): List[Int] = {
      if (n==0 || lst==Nil) {
        Nil
      } else {
        lst.head::nfirst(lst.tail, n-1)
      }
    }
    def reverseRec(l:List[Int], tmp:List[Int]):List[Int] = {
      if (l == Nil) {
        tmp
      } else {
        reverseRec(l.tail, l.head::tmp)
      }
    }

    return reverseRec(nfirst(reverseRec(l, Nil), n), Nil)
  /* END SOLUTION */
  }
  /* END TEMPLATE */
}
