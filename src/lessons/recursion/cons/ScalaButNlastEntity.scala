package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaButNlastEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( butNlast( t.getParameter(0).asInstanceOf[Array[Int]].toList, t.getParameter(1).asInstanceOf[Int] ) );
	}

	/* BEGIN TEMPLATE */
	def butNlast(l:List[Int], n:Int): List[Int] = {
	/* BEGIN SOLUTION */
  def nFirst(l:List[Int], n:Int): List[Int] = {
    if (n<=0 || l==Nil) return Nil
    return l.head::nFirst(l.tail, n-1)
  }
  return nFirst(l, l.size-n)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
