package lessons.turtleart;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class DiskTwoEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		for (int i=0;i<9;i++) {
			setColor(Color.BLACK);
			quadrant();
			setColor(Color.RED);
			quadrant();
		}
	}
	public void quadrant() {
		for (int i=0; i<20;i++) {
			forward(100);
			backward(100);
			right(1);
		}		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
