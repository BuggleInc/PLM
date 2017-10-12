/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2

import plm.universe.bat.{BatEntity, BatTest}

class ScalaFizzBuzzEntity extends BatEntity {
  /* BEGIN TEMPLATE */
  def fizzBuzz(a: Int): String = {
    /* BEGIN SOLUTION */
    (a % 5, a % 3) match {
      case (0, 0) => return "Fizz Buzz"
      case (_, 0) => return "Fizz"
      case (0, _) => return "Buzz"
      case _ => return "" + a
    }
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(fizzBuzz(t.getParameter(0).asInstanceOf[Int]))
  }
}
