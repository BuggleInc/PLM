package lessons.turtleart;

import plm.universe.turtles.Turtle;

public class HouseManyEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
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
	void nextLine() {
	     penUp();
	     left(90);
	     forward(200);
	     right(90);
	     forward(75);
	     penDown();    
	}
	void line() {
	     for (int cpt=0;cpt<4;cpt++) {
	        house(30);
	        leveCrayon();
	        right(90);
	        forward(50);
	        left(90);
	        penDown();
	     }
	     nextLine();
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
