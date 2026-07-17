package lessons.bat.string1

import plm.core.ValueSerializer
import plm.universe.bat.BatEntity

class Last2Entity extends BatEntity {

    override def run() {
      import ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(last2(param(0).asInstanceOf[String])) )
    }
	}

	/* BEGIN TEMPLATE */
	def last2(str:String):Int = {
  	/* BEGIN SOLUTION */
    val l = str.length
    if (l < 2)
      return 0
    val end = str.substring(l-2,l)
    var count = 0
    for (i <- 0 to str.length-3)
      if (str.substring(i,i+2) == end)
        count += 1
    return count
  	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
