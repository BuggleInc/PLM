package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaHouseThreeEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
	    addSizeHint(35,120, 35,150);
	    addSizeHint(80,150, 100,150);
	 
	     for (i <- 1 to 4) {
	        house(30);
	        leveCrayon();
	        right(90);
	        forward(50);
	        left(90);
	        penDown();
	     }
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
