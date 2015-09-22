package lessons.recursion.logo.polygonfractal;

import plm.universe.turtles.Turtle;

class ScalaPolygonFractalEntity extends Turtle {

	/* BEGIN TEMPLATE */
	def polygonFractal (levels:Int, sides:Int, length:Double, shrink:Double) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			/* do nothing */
		} else {
			for (i <- 1 to sides) {
				forward(length);

				left((sides-2)*360/(sides*2));
				polygonFractal(levels-1, sides, length*shrink,shrink);
				right((sides-2)*360/(sides*2));
				right(360/sides);

			}
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	override def run() {
		polygonFractal(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Int],
		    getParam(2).asInstanceOf[Double],getParam(3).asInstanceOf[Double]);
	}
}
