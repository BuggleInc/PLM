package lessons.welcome.array.island

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaIslandEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(island(param(0).asInstanceOf[Array[Int]]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def island(num:Array[Int]): Int = {
		/* BEGIN SOLUTION */
	  var nbIsland=0;
	  for (i <- 0 to num.length-2){
	    if (num(i)<num(i+1))
	      nbIsland=nbIsland+1;
	  }
	  return nbIsland
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
