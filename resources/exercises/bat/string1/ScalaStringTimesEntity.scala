package bat.string1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaStringTimesEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def stringTimes(str: String, n: Int): String = {
    /* BEGIN SOLUTION */
    var res = ""
    for (_ <- 1 to n)
      res ++= str
    res
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(stringTimes(t.getParameter(0).asInstanceOf[String], t.getParameter(1).asInstanceOf[Int]) )
  }
}
