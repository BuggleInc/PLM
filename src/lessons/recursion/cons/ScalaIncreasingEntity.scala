package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity
import lessons.recursion.cons.universe.RecList

class ScalaIncreasingEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( increasing( t.getParameter(0).asInstanceOf[Array[Int]].toList ) );
	}

	/* BEGIN TEMPLATE */
	def increasing(l:List[Int]): Boolean = {
	/* BEGIN SOLUTION */
  if (l == Nil || l.tail == Nil) return true
  if (l.head > l.tail.head)      return false
  return increasing(l.tail)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
