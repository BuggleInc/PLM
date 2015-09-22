package lessons.recursion.logo.dragoncurve;

import java.awt.Color;

import plm.universe.turtles.Turtle;

class ScalaDragonCurve2Entity extends Turtle {

	/* BEGIN TEMPLATE */
	def dragon(order:Int, x:Double, y:Double, z:Double, t:Double) {
		/* BEGIN HIDDEN */

		if (order == 1) {
			setColor(Color.red);
			moveTo(z, t);
		} else {
			val u = (x + z + t - y) / 2;
			val v = (y + t - z + x) / 2;
			dragon(order - 1, x, y, u, v);
			dragonInverse(order - 1, u, v, z, t);
		}
		/* END HIDDEN */
	}

	def dragonInverse(order:Int, x:Double, y:Double, z:Double, t:Double) {
		/* BEGIN HIDDEN */

		if (order == 1) {
			setColor(Color.blue);
			moveTo(z, t);
		} else {
			val u = (x + z - t + y) / 2;
			val v = (y + t + z - x) / 2;
			dragon(order - 1, x, y, u, v);
			dragonInverse(order - 1, u, v, z, t);
		}
		/* END HIDDEN */
	}
	/* END TEMPLATE */

	override def run() {
		dragon(getParam(0).asInstanceOf[Int], getParam(1).asInstanceOf[Double], getParam(2).asInstanceOf[Double], 
		     getParam(3).asInstanceOf[Double], getParam(4).asInstanceOf[Double]);
	}

}
