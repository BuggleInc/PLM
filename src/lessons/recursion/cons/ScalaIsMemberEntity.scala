package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaIsMemberEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( isMember( t.getParameter(0).asInstanceOf[Array[Int]].toList, t.getParameter(1).asInstanceOf[Int] ) );
	}

	/* BEGIN TEMPLATE */
	def isMember(l:List[Int], v:Int): Boolean = {
	/* BEGIN SOLUTION */
  l match {
    case a::b if a==v => true
    case a::b         => isMember(b,v)
    case _    => false
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
