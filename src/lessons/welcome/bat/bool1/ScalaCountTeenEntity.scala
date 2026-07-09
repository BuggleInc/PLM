package lessons.welcome.bat.bool1

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaCountTeenEntity extends BatEntity {

    override def run() {
      import plm.core.ValueSerializer._

      val count = getTestCount()
      for (i <- 0 to count -1) {
        val param = deserialize(getTest(i)).asInstanceOf[Array[Object]]
      	setTestResult(i, serialize(countTeen(param(0).asInstanceOf[Int],param(1).asInstanceOf[Int],param(2).asInstanceOf[Int],param(3).asInstanceOf[Int]) ))
	  }
	}

	/* BEGIN TEMPLATE */
	def countTeen(a:Int, b:Int,c:Int,d:Int): Int = {
	/* BEGIN SOLUTION */
  var ret=0;
  if (a>12&&a<20)
	   ret+=1;
  if (b>12&&b<20)
	   ret+=1;
  if (c>12&&c<20)
	   ret+=1;
  if (d>12&&d<20)
	   ret+=1;
  return ret
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
