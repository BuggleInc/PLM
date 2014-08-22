package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class HouseThreeEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
	    addSizeHint(35,120, 35,150);
	    addSizeHint(80,150, 100,150);
	 
	     for (int i=0;i<4;i++) {
	        house(30);
	        leveCrayon();
	        right(90);
	        forward(50);
	        left(90);
	        penDown();
	     }
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
