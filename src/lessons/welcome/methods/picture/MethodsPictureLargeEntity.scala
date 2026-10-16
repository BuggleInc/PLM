package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;

class MethodsPictureLargeEntity extends SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		for (i <- 1 to 9) {
			makeLine(9);
			nextLine();
		}
	}
	def mark(): Unit = {
		brushDown();
		brushUp();
	}

	def makeV(c:Color): Unit = {
		setBrushColor(c);
		step();
		mark();

		step();
		left();
		step();
		mark();

		stepBack();
		right();
		step();
		mark();

		step();
		left();
	}

	def makePattern(): Unit = {
		makeV(Color.YELLOW);
		makeV(Color.RED);
		makeV(Color.BLUE);
		makeV(Color.GREEN);
		forward(5);
	}

	def makeLine(count: Int): Unit = {
		for (i<- 1 to count)
			makePattern();
		backward(count*5);
	}

	def nextLine(): Unit = {
		left();
		forward(5);
		right();	
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
