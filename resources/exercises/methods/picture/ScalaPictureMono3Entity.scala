package lessons.welcome.methods.picture;

import plm.universe.bugglequest.SimpleBuggle;

class ScalaPictureMono3Entity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		for (i <- 1 to 9) {
			makeLine(9);
			nextLine();
		}
	}
	def mark() {
		brushDown();
		brushUp();
	}

	def makeV() {
		forward(2);
		mark();

		forward();
		left();
		forward();
		mark();

		backward();
		right();
		forward();
		mark();

		forward(2);
		left();
	}

	def makePattern() {
		makeV();
		makeV();
		makeV();
		makeV();
		forward(7);
	}

	def makeLine(count: Int){
		for (i <- 1 to count)
			makePattern();
		backward(count*7);
	}

	def nextLine() {
		left();
		forward(7);
		right();	
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
