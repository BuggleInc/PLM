package lessons.welcome.array.golomb

import plm.universe.bat.BatEntity
import plm.universe.bat.BatTest

class ScalaGolombEntity extends BatEntity {

	override def run(t: BatTest) {
		t.setResult( golomb(t.getParameter(0).asInstanceOf[Int]) );
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
