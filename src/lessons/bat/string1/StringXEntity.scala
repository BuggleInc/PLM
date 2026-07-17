package lessons.bat.string1

import plm.core.ValueSerializer
import plm.universe.bat.BatEntity

class StringXEntity extends BatEntity {

    override def run() {
      import ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(stringX(param(0).asInstanceOf[String])) )
	  }
	}

	/* BEGIN TEMPLATE */
	def stringX(str:String):String = {
		/* BEGIN SOLUTION */
	  var res = ""
	  for (i <- 0 to str.length-1)
	    if (str(i) != 'x' || i == 0 || i == str.length-1)
	      res += str.substring(i,i+1)
	  return res
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
