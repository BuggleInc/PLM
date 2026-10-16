package lessons.welcome.bdr;

class BDREntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
	    var done = false
		while (!done) {
			var c = readMessage()

			if (c == "R") { 
				right(); step();
			} else if (c == "L") {
				left(); step();
			} else if (c == "I") {
				back(); step();
			} else if (c == "A")
				step();
			else if (c == "B")
				forward(2);
			else if (c == "C")
				forward(3);
			else if (c == "Z")
				stepBack();
			else if (c == "Y")
				backward(2);
			else if (c == "X")
				backward(3);
			else 
				done = true ;
		}		
		/* END SOLUTION */
	}
	/* END REMOTE */
}
