package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaNlastEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( nlast( t.getParameter(0).asInstanceOf[Array[Int]].toList, t.getParameter(1).asInstanceOf[Int] ) );
	}

	/* BEGIN TEMPLATE */
	def nlast(l:List[Int], n:Int): List[Int] = {
	/* BEGIN SOLUTION */
  def ButnFirst(l:List[Int], n:Int): List[Int] = {
    if (n<=0 || l==Nil) return l
    return ButnFirst(l.tail, n-1)
  }
  return ButnFirst(l, l.size-n)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
