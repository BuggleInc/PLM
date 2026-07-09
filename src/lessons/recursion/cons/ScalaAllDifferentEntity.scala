package lessons.recursion.cons

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest
import lessons.recursion.cons.universe.ConsEntity

class ScalaAllDifferentEntity extends ConsEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(allDifferent( param(0).asInstanceOf[Array[Int]].toList )) )
	  }
	}

	/* BEGIN TEMPLATE */
	def allDifferent(l:List[Int]): Boolean = {
	/* BEGIN SOLUTION */
  if (l == Nil)                 return true
  if (isMember(l.tail, l.head)) return false
  return allDifferent(l.tail)
}
def isMember(l:List[Int], v:Int): Boolean = {
  if (l == Nil)    return false
  if (v == l.head) return true
  return isMember(l.tail, v)
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
