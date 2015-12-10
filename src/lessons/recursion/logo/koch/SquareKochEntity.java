package lessons.recursion.logo.koch;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class SquareKochEntity extends Turtle {

	/* BEGIN TEMPLATE */
	void snowSquare (int levels, double length) {
		squareSide(levels, length);
		right(90);
		setColor(Color.blue);
		squareSide(levels, length);
		right(90);
		setColor(Color.orange);
		squareSide(levels, length);
		right(90);
		setColor(Color.magenta);
		squareSide(levels, length);
		right(90);
	}
	void squareSide(int levels, double length) {
		/* BEGIN SOLUTION */
		if (levels == 0) {
			forward(length);
		} else {
			squareSide(levels-1, length/3);
			left(90);
			squareSide(levels-1, length/3);
			right(90);
			squareSide(levels-1, length/3);
			right(90);
			squareSide(levels-1, length/3);
			left(90);
			squareSide(levels-1, length/3);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */
	public void drawHint() {
	    clear();
	    hide();
	    setColor(Color.red);
	    setPos(100,50);setHeading(0);
	    squareSide(0,200);
	    
	    setPos(100,150); setHeading(0);
	    squareSide(1,200);

	    setPos(100,250); setHeading(0);
	    squareSide(2,200);
	    
	    setPos(100,350); setHeading(0);
	    squareSide(3,200);	    
	}

	public void run() {
		snowSquare((Integer)getParam(0),(Double)getParam(1));
	}
}
