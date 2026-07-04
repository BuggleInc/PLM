package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaButLastEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( butLast( t.getParameter(0).asInstanceOf[Array[Int]].toList ) );
	}

	/* BEGIN TEMPLATE */
	def butLast(l:List[Int]): List[Int] = {
	/* BEGIN SOLUTION */
  l match {
    case a::b if b==Nil => Nil
    case a::b           => a::butLast(b)
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
