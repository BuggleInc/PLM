package lessons.recursion.lego.dragoncurve;

import java.awt.Color;

import plm.universe.turtles.Turtle;

class DragonCurve2Entity extends Turtle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	def dragon(order:Int, x:Double, y:Double, z:Double, t:Double): Unit = {
		/* BEGIN SOLUTION */

		if (order == 1) {
			setColor(Color.red);
			moveTo(z, t);
		} else {
			val u = (x + z + t - y) / 2;
			val v = (y + t - z + x) / 2;
			dragon(order - 1, x, y, u, v);
			dragonInverse(order - 1, u, v, z, t);
		}
		/* END SOLUTION */
	}

	def dragonInverse(order:Int, x:Double, y:Double, z:Double, t:Double): Unit = {
		/* BEGIN SOLUTION */

		if (order == 1) {
			setColor(Color.blue);
			moveTo(z, t);
		} else {
			val u = (x + z - t + y) / 2;
			val v = (y + t + z - x) / 2;
			dragon(order - 1, x, y, u, v);
			dragonInverse(order - 1, u, v, z, t);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run(): Unit = {
		dragon(getParamInt(0), getParamDouble(1), getParamDouble(2), 
		     getParamDouble(3), getParamDouble(4));
	}
	/* END REMOTE */

}
