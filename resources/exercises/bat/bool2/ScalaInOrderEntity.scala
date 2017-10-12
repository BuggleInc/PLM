/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package bat.bool2
import plm.universe.bat.{BatEntity, BatTest}

public class ScalaInOrderEntity extends BatEntity {
	/* BEGIN TEMPLATE */
	def inOrder(a:Int, b:Int, c:Int, bOk:Boolean):Boolean = {
		/* BEGIN SOLUTION */
		(bOk || (b > a)) && (c > b)
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(t: BatTest) {
		t.setResult( inOrder(t.getParameter(0).asInstanceOf[Int], t.getParameter(1).asInstanceOf[Int],
			t.getParameter(2).asInstanceOf[Int], t.getParameter(3).asInstanceOf[Boolean]) )
	}

}
