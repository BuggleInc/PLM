package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaHouseManyEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
	    addSizeHint(35,220, 35,250);
	    addSizeHint(80,250, 100,250);
	    addSizeHint(20,250-75, 20, 250);
	 
	    line();
	    house(30);
	    penUp();right(90);forward(150);left(90);penDown();
	    house(30);
	    penUp();
	    left(90);
	    forward(150);
	    right(90);
	    forward(75);
	    penDown();
	    line();
	}
	def nextLine() {
	     penUp();
	     left(90);
	     forward(200);
	     right(90);
	     forward(75);
	     penDown();    
	}
	def line() {
	     for (i <- 1 to 4) {
	        house(30);
	        leveCrayon();
	        right(90);
	        forward(50);
	        left(90);
	        penDown();
	     }
	     nextLine();
	}
	def house(len:Int) {
	    forward(len);
	    
	    right(30);
	    for (i <- 1 to 3) {
	    	forward(len);
	    	right(120);
	    }
	    
	    right(60);    
	    for (i <- 1 to 3) {
	    	forward(len);
	    	right(90);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
