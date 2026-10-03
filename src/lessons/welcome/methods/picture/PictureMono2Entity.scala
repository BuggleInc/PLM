package lessons.welcome.methods.picture;


class PictureMono2Entity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		for (i <- 1 to 3) {
			makeLine(3);
			nextLine();
		}
	}
	def mark(): Unit = {
		brushDown();
		brushUp();
	}

	def makeV(): Unit = {
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

	def makePattern(): Unit = {
	  for (i <- 1 to 4) {
		  makeV();
	  }
	  forward(7);
	}

	def makeLine(count: Int): Unit = {
		for (i <- 1 to count)
			makePattern();
		backward(count*7);
	}

	def nextLine(): Unit = {
		left();
		forward(7);
		right();	
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
