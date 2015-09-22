package lessons.recursion.logo.dragoncurve;

import plm.universe.turtles.Turtle;

class ScalaDragonCurve1Entity extends Turtle {

	/* BEGIN TEMPLATE */
	def dragon(order:Int, x:Double, y:Double, z:Double, t:Double) {
		/* BEGIN SOLUTION */
		if (order == 1) {
			setPos(x, y);
			moveTo(z, t);
		} else {
			val u = (x + z + t - y) / 2;
			val v = (y + t - z + x) / 2;
			dragon(order - 1, x, y, u, v);
			dragon(order - 1, z, t, u, v);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() {
		dragon(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Double], getParam(2).asInstanceOf[Double], 
		    getParam(3).asInstanceOf[Double], getParam(4).asInstanceOf[Double]);
	}

}
