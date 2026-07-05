package lessons.welcome.methods.picture;


class ScalaPictureMono2Entity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		for (i <- 1 to 3) {
			makeLine(3);
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

		stepForward();
		left();
		stepForward();
		mark();

		stepBackward();
		right();
		stepForward();
		mark();

		forward(2);
		left();
	}

	def makePattern() {
	  for (i <- 1 to 4) {
		  makeV();
	  }
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
