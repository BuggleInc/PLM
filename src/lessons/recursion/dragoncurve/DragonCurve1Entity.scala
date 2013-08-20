package lessons.recursion.dragoncurve;

import jlm.universe.turtles.Turtle;

class ScalaDragonCurve1Entity extends Turtle {

	/* BEGIN TEMPLATE */
	def dragon(ordre:Int, x:Double, y:Double, z:Double, t:Double) {
		/* BEGIN SOLUTION */
		if (ordre == 1) {
			setPos(x, y);
			moveTo(z, t);
		} else {
			val u = (x + z + t - y) / 2;
			val v = (y + t - z + x) / 2;
			dragon(ordre - 1, x, y, u, v);
			dragon(ordre - 1, z, t, u, v);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() {
		dragon(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Double], getParam(2).asInstanceOf[Double], 
		    getParam(3).asInstanceOf[Double], getParam(4).asInstanceOf[Double]);
	}

}
