package lessons.bat.string1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaStringSplosionEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def stringSplosion(str: String): String = {
    /* BEGIN SOLUTION */
    var res = ""
    for (i <- 0 to str.length - 1)
      res ++= str.substring(0, i + 1)
    return res
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(stringSplosion(t.getParameter(0).asInstanceOf[String]))
  }
}
