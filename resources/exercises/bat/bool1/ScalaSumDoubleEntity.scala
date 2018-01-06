package bat.bool1

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

class ScalaSumDoubleEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( sumDouble(t.getParameter(0).asInstanceOf[Integer],t.getParameter(1).asInstanceOf[Integer]))
  }

  /* BEGIN TEMPLATE */
  def sumDouble(a: Integer, b: Integer): Integer = {
    /* BEGIN SOLUTION */
    if(a==b) 
      (a+b)*2
    else 
      a+b
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}