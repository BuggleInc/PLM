package lessons.bat.string1

import plm.core.ValueSerializer
import plm.universe.bat.BatEntity

class StringTimesEntity extends BatEntity {

    override def run() {
      import ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(stringTimes(param(0).asInstanceOf[String], param(1).asInstanceOf[Int])) )
	  }
	}

	/* BEGIN TEMPLATE */
	def stringTimes(str:String, n:Int):String = {
		/* BEGIN SOLUTION */
	  var res = ""
	  for (i <- 1 to n)
	    res ++= str
	  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
