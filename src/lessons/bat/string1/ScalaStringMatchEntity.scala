package lessons.bat.string1

import plm.core.ValueSerializer
import plm.universe.bat.BatEntity

class ScalaStringMatchEntity extends BatEntity {

    override def run() {
      import ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(stringMatch(param(0).asInstanceOf[String], param(1).asInstanceOf[String])) )
	  }
	}

	/* BEGIN TEMPLATE */
	def stringMatch(a:String, b:String):Int = {
	  /* BEGIN SOLUTION */
	  val l = Math.min( a.length, b.length )
	  var count = 0
	  for (i <- 0 to l-2)
	    if (a.substring(i,i+2) == b.substring(i,i+2))
	      count += 1
	  return count
	  /* END SOLUTION */
	}
	/* END TEMPLATE */
}
