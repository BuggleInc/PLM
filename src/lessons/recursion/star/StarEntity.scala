package lessons.recursion.star;

import java.awt.Color;

import jlm.universe.turtles.Turtle;

class ScalaStarEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		star(100, Color.black);
		right(45);
		star(80, Color.blue);
		right(45);
		star(60, Color.red);
	}
	def branch(size:Int) {
		forward(size);
		right(360 / BRANCH_COUNT);
		forward(size);

		for (i <- 1 to 2)
			left(360 / BRANCH_COUNT);
	}

	val BRANCH_COUNT = 5;
	def star(size:Int, c:Color) {
		setColor(c);
		for (i <- 1 to BRANCH_COUNT) {
			branch(size);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
