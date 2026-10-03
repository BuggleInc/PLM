package lessons.turtleart;

import plm.universe.turtles.Turtle;

class HouseThreeEntity extends Turtle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
	    addSizeHint(35,120, 35,150);
	    addSizeHint(80,150, 100,150);
	 
	     for (i <- 1 to 4) {
	        house(30);
	        penUp();
	        right(90);
	        forward(50);
	        left(90);
	        penDown();
	     }
	}
	def house(len:Int): Unit = {
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
	/* END REMOTE */
}
