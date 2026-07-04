package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaNfirstEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( nfirst( t.getParameter(0).asInstanceOf[Array[Int]].toList, t.getParameter(1).asInstanceOf[Int] ) );
	}

	/* BEGIN TEMPLATE */
	def nfirst(l:List[Int], n:Int): List[Int] = {
	/* BEGIN SOLUTION */
  if (n == 0) return Nil
  else        return l.head :: nfirst(l.tail, n-1)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
