package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaNthEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( nth( t.getParameter(0).asInstanceOf[Array[Int]].toList, t.getParameter(1).asInstanceOf[Int] ) );
	}

	/* BEGIN TEMPLATE */
	def nth(l:List[Int], n:Int): Int = {
	/* BEGIN SOLUTION */
  if (n == 1) return l.head
  else        return nth(l.tail, n-1)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
