package lessons.turtleart;

import plm.universe.turtles.Turtle;

class ScalaHouseEntity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
	    addSizeHint(50,265, 150,265);
	    addSizeHint(35,250, 35,150);
	 
	    house(100);
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
