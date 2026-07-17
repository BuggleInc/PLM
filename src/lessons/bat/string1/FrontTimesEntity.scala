package lessons.bat.string1

import plm.core.ValueSerializer
import plm.universe.bat.BatEntity

class FrontTimesEntity extends BatEntity {

    override def run() {
      import ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(frontTimes(param(0).asInstanceOf[String], param(1).asInstanceOf[Int])) )
	  }
	}

	/* BEGIN TEMPLATE */
	def frontTimes(str:String, n:Int):String = {
	/* BEGIN SOLUTION */
	  var frontLen = 3
	  if (frontLen > str.length)
	    frontLen = str.length
	  var front = ""
	  if (str.length >0)
	    front = str.substring(0,frontLen)
	  return front * n
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
