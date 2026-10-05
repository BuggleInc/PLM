package lessons.recursion.lego.dragoncurve;

import plm.universe.turtles.Turtle;

class DragonCurve1Entity extends Turtle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	def dragon(order:Int, x:Double, y:Double, z:Double, t:Double): Unit = {
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

	override def run(): Unit = {
		dragon(getParamInt(0), getParamDouble(1), getParamDouble(2), 
		    getParamDouble(3), getParamDouble(4));
	}
	/* END REMOTE */

}
