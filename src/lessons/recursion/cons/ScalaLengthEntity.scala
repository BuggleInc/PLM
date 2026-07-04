package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaLengthEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( length( t.getParameter(0).asInstanceOf[Array[Int]].toList ) );
	}

	/* BEGIN TEMPLATE */
	def length(l:List[Int]): Int = {
	/* BEGIN SOLUTION */
  l match {
    case a::b => 1+length(b)
    case _    => 0
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
