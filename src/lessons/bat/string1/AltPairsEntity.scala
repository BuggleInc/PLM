package lessons.bat.string1

import plm.core.ValueSerializer
import plm.universe.bat.BatEntity

class AltPairsEntity extends BatEntity {

    override def run() {
      import ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
        setTestResult(i, serialize(altPairs(param(0).asInstanceOf[String])))
      }
    }

    /* BEGIN TEMPLATE */
    def altPairs(str: String): String = {
        /* BEGIN SOLUTION */
        var res = ""
        for (i <- 0 to (str.length - 1) by 4)
            res ++= str.substring(i, Math.min(i + 2, str.length))
        return res
        /* END SOLUTION */
    }
    /* END TEMPLATE */
}
