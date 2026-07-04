package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaOccurrenceEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( occurences( t.getParameter(0).asInstanceOf[Array[Int]].toList, t.getParameter(1).asInstanceOf[Int] ) );
	}

	/* BEGIN TEMPLATE */
	def occurences(l:List[Int], v:Int): Int = {
	/* BEGIN SOLUTION */
  l match {
    case a::b if a==v => 1 + occurences(b,v)
    case a::b         =>     occurences(b,v)
    case _    => 0
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
