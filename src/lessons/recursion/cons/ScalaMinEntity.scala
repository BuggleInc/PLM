package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaMinEntity extends ConsEntity {

	override def run(t: BatTest) {
		t.setResult( min( t.getParameter(0).asInstanceOf[Array[Int]].toList ) );
	}

	/* BEGIN TEMPLATE */
	def min(l:List[Int]): Int = {
	/* BEGIN SOLUTION */
  def min2(l:List[Int], v:Int): Int = {
    if (l==Nil) return v
    if (l.head < v) return min2(l.tail, l.head)
    return min2(l.tail, v)
  }
  return min2(l.tail, l.head)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
