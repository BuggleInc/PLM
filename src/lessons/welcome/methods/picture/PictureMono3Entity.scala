package lessons.welcome.methods.picture;

import jlm.universe.bugglequest.SimpleBuggle;

class ScalaPictureMono3Entity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	def mark() {
		brushDown();
		brushUp();
	}

	def makeV() {
		forward();
		mark();

		forward();
		left();
		forward();
		mark();

		backward();
		right();
		forward();
		mark();

		forward();
		left();
	}

	def makePattern() {
		makeV();
		makeV();
		makeV();
		makeV();
		forward(5);
	}

	def makeLine(count: Int){
		for (i <- 1 to count)
			makePattern();
		backward(count*5);
	}

	def nextLine() {
		left();
		forward(5);
		right();	
	}

	def run() {
		for (i <- 1 to 9) {
			makeLine(9);
			nextLine();
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
