package lessons.welcome.array.golomb

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaGolombEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(golomb(param(0).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def golomb(num:Int): Int = {
		/* BEGIN SOLUTION */
	  if(num==1)
	  		return 1;
	  else
	  		return 1+golomb(num-golomb(golomb(num-1)));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
