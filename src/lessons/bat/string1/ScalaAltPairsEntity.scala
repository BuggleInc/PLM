package lessons.bat.string1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaAltPairsEntity extends BatEntity {

    override def run(t: BatTest) {
        t.setResult(altPairs(t.getParameter(0).asInstanceOf[String]))
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
