package array.island

import plm.universe.bat.BatTest
import plm.universe.bat.BatEntity

class ScalaIslandEntity extends BatEntity {
  override def run(t: BatTest) {
    t.setResult( island(t.getParameter(0).asInstanceOf[Array[Int]]))
  }

  /* BEGIN TEMPLATE */
  def island(num:Array[Int]): Int = {
    /* BEGIN SOLUTION */
    var nbIsland=0;
		for (i <- 0 to num.length-2){
			if (num(i)<num(i+1))
		    nbIsland=nbIsland+1;
		}
		nbIsland
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}