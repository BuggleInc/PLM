package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaLastEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( last( t.getParameter(0).asInstanceOf[Array[Int]].toList ) );
	}

	/* BEGIN TEMPLATE */
	def last(l:List[Int]): Int = {
	/* BEGIN SOLUTION */
  l match {
    case a::b if b==Nil => a
    case a::b            => last(b)
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
