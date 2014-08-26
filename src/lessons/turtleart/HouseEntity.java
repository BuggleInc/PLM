package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class HouseEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
	    addSizeHint(50,265, 150,265);
	    addSizeHint(35,250, 35,150);
	 
	    house(100);
	}
	void house(int len) {
	    forward(len);
	    
	    right(30);
	    for (int i = 0; i < 3; i++) {
	    	forward(len);
	    	right(120);
	    }
	    
	    right(60);    
	    for (int i = 0; i < 3; i++) {
	    	forward(len);
	    	right(90);
	    }
	    /* END SOLUTION */
	}
	/* END TEMPLATE */
}
