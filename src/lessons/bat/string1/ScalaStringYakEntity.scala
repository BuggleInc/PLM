package lessons.bat.string1

import plm.core.ValueSerializer
import plm.universe.bat.BatEntity

class ScalaStringYakEntity extends BatEntity {

    override def run() {
      import ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      setTestResult(i, serialize(stringYak(param(0).asInstanceOf[String])) )
    }
	}

	/* BEGIN TEMPLATE */
	def stringYak(str:String):String = {
  	/* BEGIN SOLUTION */
    var res = ""
    var i=0
    while (i<str.length) {
      if (i+2<str.length  && str(i) == 'y' && str(i+2)=='k')
        i += 2
      else
        res += str(i)
      i+=1
    }
    return res
  	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
