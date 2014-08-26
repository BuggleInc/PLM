package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaStarEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		addSizeHint(165, 200, 165, 150)

		for (i <- 1 to BRANCH_COUNT) 
			branch(50);
		
	}
	val BRANCH_COUNT = 5;
	def branch(size:Int) {
		forward(size);
		right(360 / BRANCH_COUNT);
		forward(size);

		for (i <- 1 to 2)
			left(360 / BRANCH_COUNT);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
