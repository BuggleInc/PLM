package lessons.welcome.bdr;

class ScalaBDREntity extends plm.universe.bugglequest.SimpleBuggle {

	override def run() {
		/* BEGIN SOLUTION */
	    var done = false
		while (!done) {
			var c = readMessage()

			if (c == "R") { 
				right(); stepForward();
			} else if (c == "L") {
				left(); stepForward();
			} else if (c == "I") {
				back(); stepForward();
			} else if (c == "A")
				stepForward();
			else if (c == "B")
				forward(2);
			else if (c == "C")
				forward(3);
			else if (c == "Z")
				stepBackward();
			else if (c == "Y")
				backward(2);
			else if (c == "X")
				backward(3);
			else 
				done = true ;
		}		
		/* END SOLUTION */
	}
}
