package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaRemoveEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( remove(t.getParameter(0).asInstanceOf[Array[Int]].toList, t.getParameter(1).asInstanceOf[Int]) );
	}

	/* BEGIN TEMPLATE */
	def remove(l:List[Int], v:Int): List[Int] = {
	/* BEGIN SOLUTION */
  l match {
    case a::_ if a==v => remove(l.tail, v)
    case a::b         => a::remove(b, v)
    case _            => Nil
  }
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
