package bat.string1

import plm.universe.bat.{BatEntity, BatTest}

class ScalaFrontTimesEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def frontTimes(str: String, n: Int): String = {
    /* BEGIN SOLUTION */
    var frontLen = 3
    if (frontLen > str.length)
      frontLen = str.length
    var front = ""
    if (str.length > 0)
      front = str.substring(0, frontLen)
    return front * n
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(frontTimes(t.getParameter(0).asInstanceOf[String], t.getParameter(1).asInstanceOf[Int]))
  }
}
