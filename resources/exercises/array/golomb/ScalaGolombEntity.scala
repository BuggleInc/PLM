package array.golomb

import plm.universe.bat.{BatEntity, BatTest}

class ScalaGolombEntity extends BatEntity {

  /* BEGIN TEMPLATE */
  def golomb(num: Int): Int = {
    /* BEGIN SOLUTION */
    if (num == 1)
      1
    else
      1 + golomb(num - golomb(golomb(num - 1)))
    /* END SOLUTION */
  }

  /* END TEMPLATE */

  override def run(t: BatTest) {
    t.setResult(golomb(t.getParameter(0).asInstanceOf[Int]))
  }
}

