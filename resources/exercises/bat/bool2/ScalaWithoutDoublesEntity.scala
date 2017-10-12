package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaWithoutDoublesEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def withoutDoubles(die1: Int, die2: Int, noDoubles: Boolean): Int = {
    /* BEGIN SOLUTION */
    if (noDoubles && (die1 == die2)) {
      if (die1 == 6)
        return 1 + die2
      else
        return die1 + 1 + die2
    } else
      return die1 + die2
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(withoutDoubles(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int],
                               t.getParameter(2).asInstanceOf[Boolean]) )
  }
}
