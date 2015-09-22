package lessons.recursion.logo.koch;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class KochEntity extends Turtle {
	/* BEGIN TEMPLATE */
	void snowFlake (int levels, double length) {
		snowSide(levels, length);
		right(120);
		setColor(Color.blue);
		snowSide(levels, length);
		right(120);
		setColor(Color.orange);
		snowSide(levels, length);
		right(120);
	}
	void snowSide(int levels, double length) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			snowSide(levels-1, length/3);
			left(60);
			snowSide(levels-1, length/3);
			right(120);
			snowSide(levels-1, length/3);
			left(60);
			snowSide(levels-1, length/3);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */
	
	public void run() {
		snowFlake((Integer)getParam(0),(Double)getParam(1));
	}
	
	public void drawHint() {
	    clear();
	    hide();
	    setColor(Color.red);
	    setPos(100,50);setHeading(0);
	    snowSide(0,200);
	    
	    setPos(100,150); setHeading(0);
	    snowSide(1,200);

	    setPos(100,250); setHeading(0);
	    snowSide(2,200);
	    
	    setPos(100,350); setHeading(0);
	    snowSide(3,200);	    
	}

}
